package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.ChatroomType;
import com.ourdressingtable.chat.domain.Message;
import com.ourdressingtable.chat.domain.QChat;
import com.ourdressingtable.chat.domain.QChatroom;
import com.ourdressingtable.chat.domain.QMessage;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                    .build();
        }).toList();
    }
}
