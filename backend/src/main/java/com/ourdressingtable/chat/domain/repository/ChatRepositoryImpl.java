package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.QChat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findOneToOneChatroom(Long memberId, Long targetId) {
        QChat chat = QChat.chat;
        QChat targetChat = new QChat("targetChat");

        Long chatroomId = queryFactory
                .select(chat.chatroom.id)
                .from(chat)
                .join(targetChat).on(chat.chatroom.id.eq(targetChat.id))
                .where(
                        chat.member.id.eq(memberId),
                        targetChat.member.id.eq(targetId),
                        chat.isActive.isTrue(),
                        targetChat.isActive.isTrue()
                ).fetchOne();

        return Optional.ofNullable(chatroomId);
    }
}
