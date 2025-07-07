package com.ourdressingtable.chat.domain.repository;

import java.time.LocalDateTime;

public interface MessageRepositoryCustom {
    long countUnreadMessages(Long chatroomId, Long memberId, LocalDateTime lastReadAt);

}
