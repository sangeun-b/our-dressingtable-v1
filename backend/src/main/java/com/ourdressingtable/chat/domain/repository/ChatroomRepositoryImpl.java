package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.*;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.QMember;
import com.ourdressingtable.member.repository.MemberRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryImpl implements ChatroomRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final ChatReadRepository chatReadRepository;
    private final MessageRepository messageRepository;

    @Override
    public List<OneToOneChatroomSummaryResponse> findOneToOneChatroomsByMemberId(Long memberId) {
        QChatroom chatroom = QChatroom.chatroom;
        QChat chat = QChat.chat;
        QMember member = QMember.member;
        QMessage message = QMessage.message;

        // 1:1 채팅방 ID 조회
        List<Long> oneToOneChatroomIds = queryFactory
                .select(chat.chatroom.id)
                .from(chat)
                .groupBy(chat.chatroom.id)
                .having(chat.count().eq(2L))
                .fetch();

        if(oneToOneChatroomIds.isEmpty()) return List.of();

        // 현재 회원이 참여 중인 채팅방 목록 조회
        List<Chatroom> chatrooms = queryFactory
                .selectFrom(chatroom)
                .where(chatroom.type.eq(ChatroomType.ONE_TO_ONE),
                        chatroom.id.in(
                                JPAExpressions
                                        .select(chat.chatroom.id)
                                        .from(chat)
                                        .where(chat.member.id.eq(memberId), chat.isActive.eq(true))
                        ),
                        chatroom.id.in(oneToOneChatroomIds))
                .fetch();

        List<Long> chatroomIds = chatrooms.stream().map(Chatroom::getId).toList();

        // Target Member 조회 (채팅방당 상대방 1명씩)
        List<Tuple> targetMemberTuples = queryFactory
                .select(chat.chatroom.id, member)
                .from(chat)
                .join(chat.member, member)
                .where(chat.chatroom.id.in(chatroomIds),
                        member.id.ne(memberId),
                        chat.isActive.eq(true))
                .fetch();

        Map<Long, Member> targetMemberMap = targetMemberTuples.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(chat.chatroom.id),
                        tuple -> tuple.get(member)
                ));

        // 마지막 메시지 조회
        List<Tuple> lastMessages = queryFactory
                .select(message.chatroom.id, message)
                .from(message)
                .where(message.chatroom.id.in(chatroomIds))
                .orderBy(message.chatroom.id.asc(), message.createdAt.desc())
                .fetch();

        // 각 채팅방의 가장 최신 메시지만 Map으로 정리
        Map<Long, Message> lastMessageMap = new LinkedHashMap<>();
        for(Tuple tuple : lastMessages){
            Long chatroomId = tuple.get(message.chatroom.id);
            if(!lastMessageMap.containsKey(chatroomId)){
                lastMessageMap.put(chatroomId,tuple.get(message));
            }
        }

        // 읽은 시간 조회
        List<ChatRead> chatReads = chatReadRepository.findByMemberIdAndChatroomIds(memberId, chatroomIds);
        Map<Long, LocalDateTime> readAtMap = chatReads.stream()
                .collect(Collectors.toMap(
                        cr -> cr.getChatroom().getId(),
                        ChatRead::getLastReadAt
                ));

        // 안 읽은 메시지 수 조회
        Map<Long, Long> unreadCountMap = new HashMap<>();
        for(Long chatroomId : chatroomIds){
            LocalDateTime lastReadAt = readAtMap.get(chatroomId);
            LocalDateTime safeLastReadAt = (lastReadAt != null) ? lastReadAt : LocalDateTime.of(1970, 1, 1, 0, 0);
            long count = messageRepository.countUnreadMessages(chatroomId, memberId, safeLastReadAt);
            unreadCountMap.put(chatroomId, count);
        }

        return chatrooms.stream().map(cr -> {
            Member target = targetMemberMap.get(cr.getId());
            Message last = lastMessageMap.get(cr.getId());
            return OneToOneChatroomSummaryResponse.builder()
                    .chatroomId(cr.getId())
                    .targetMemberId(target != null ? target.getId() : null)
                    .targetNickname(target != null ? target.getNickname() : null)
                    .targetProfileImageUrl(target != null ? target.getImageUrl() : null)
                    .lastMessage(last != null ? last.getContent() : null)
                    .lastMessageTime(last != null ? last.getCreatedAt() : null)
                    .unreadCount(unreadCountMap.getOrDefault(cr.getId(), 0L).intValue())
                    .build();
        }).toList();
    }
}
