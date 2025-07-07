package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.QMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countUnreadMessages(Long chatroomId, Long memberId, LocalDateTime lastReadAt) {
        QMessage message = QMessage.message;
        Long result =  queryFactory
                .select(message.count())
                .from(message)
                .where(
                        message.chatroom.id.eq(chatroomId),
                        message.sender.id.ne(memberId),
                        message.createdAt.gt(lastReadAt)
                )
                .fetchOne();

        return Optional.ofNullable(result).orElse(0L);
    }
}
