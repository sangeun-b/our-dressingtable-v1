package com.ourdressingtable.chat.domain;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message_reads")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRead {

    @Id
    private String id = UUID.randomUUID().toString();

    private boolean isRead = false;

    private LocalDateTime readAt;

    private String messageId;

    private String memberId;

    @Builder
    public MessageRead(boolean isRead, LocalDateTime readAt, String messageId, String memberId) {
        this.isRead = isRead;
        this.readAt = readAt;
        this.messageId = messageId;
        this.memberId = memberId;
    }
}
