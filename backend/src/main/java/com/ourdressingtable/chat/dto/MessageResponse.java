package com.ourdressingtable.chat.dto;

import com.ourdressingtable.chat.domain.Message;
import java.time.LocalDateTime;

public record MessageResponse(
        String messageId,
        String senderId,
        String content,
        LocalDateTime createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getId(), message.getSenderId(), message.getContent(), message.getCreatedAt());
    }

}
