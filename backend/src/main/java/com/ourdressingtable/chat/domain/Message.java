package com.ourdressingtable.chat.domain;

import com.ourdressingtable.common.util.MongoCreatedAtEntity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends MongoCreatedAtEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    private MessageType messageType;

    private String content;

    private boolean isSystem = false;

    private String senderId;

    private String chatroomId;

    @Builder
    public Message(String id, MessageType messageType, String content, String senderId, String chatroomId) {
        this.id = id;
        this.messageType = messageType;
        this.content = content;
        this.senderId = senderId;
        this.chatroomId = chatroomId;
    }

}
