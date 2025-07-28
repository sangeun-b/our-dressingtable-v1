package com.ourdressingtable.chat.domain.repository;

import java.time.LocalDateTime;

public interface MessageRepositoryCustom {
    long countUnreadMessages(String chatroomId, String memberId, LocalDateTime lastReadAt);

}
