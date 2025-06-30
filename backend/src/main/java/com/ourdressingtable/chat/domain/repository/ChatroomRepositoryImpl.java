package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.Message;
import com.ourdressingtable.chat.domain.QChat;
import com.ourdressingtable.chat.domain.QChatroom;
import com.ourdressingtable.chat.domain.QMessage;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.QMember;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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

        List<Long> oneToOneChatroomIds = queryFactory
                .select(chat.chatroom.id)
                .from(chat)
                .where(chat.isActive.eq(true))
                .groupBy(chat.chatroom.id)
                .having(chat.count().eq(2L))
                .fetch();

        List<Chatroom> chatrooms = queryFactory
                .selectFrom(chatroom)
                .where(
                        chatroom.id.in(oneToOneChatroomIds),
                        chatroom.id.in(
                                JPAExpressions
                                        .select(chat.chatroom.id)
                                        .from(chat)
                                        .where(chat.member.id.eq(memberId), chat.isActive.eq(true))
                        )
                )
                .fetch();
        return chatrooms.stream().map(cr -> {
            Member target = queryFactory
                    .select(member)
                    .from(chat)
                    .join(chat.member, member)
                    .where(chat.chatroom.id.eq(cr.getId()), member.id.ne(memberId),
                            chat.isActive.eq(true))
                    .fetchOne();
            Message lastMessage = queryFactory
                    .selectFrom(message)
                    .where(message.chatroom.id.eq(cr.getId()))
                    .orderBy(message.createdAt.desc())
                    .limit(1)
                    .fetchOne();
            return OneToOneChatroomSummaryResponse.builder()
                    .chatroomId(cr.getId())
                    .targetMemberId(target != null ? target.getId(): null)
                    .targetNickname(target != null ? target.getNickname() : "unknown")
                    .targetProfileImageUrl(target != null ? target.getImageUrl() : null)
                    .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
                    .lastMessageTime(lastMessage != null ? lastMessage.getCreatedAt() : null)
                    .build();

        }).toList();
    }
}
