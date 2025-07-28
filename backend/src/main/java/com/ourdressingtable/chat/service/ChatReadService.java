package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.ChatRead;

import java.time.LocalDateTime;

public interface ChatReadService {

    void markAsRead(String chatroomId);
    LocalDateTime getLastReadAt(String chatroomId);
}
