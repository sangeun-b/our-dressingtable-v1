package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.ChatRead;

import java.time.LocalDateTime;

public interface ChatReadService {

    void markAsRead(Long chatroomId);
    LocalDateTime getLastReadAt(Long chatroomId);
}
