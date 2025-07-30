package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Message;
import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepositoryCustom {
    long countUnreadMessages(String chatroomId, String memberId, LocalDateTime lastReadAt);
    List<Message> findRecentMessages(String chatroomId, int size);
}
