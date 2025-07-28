package com.ourdressingtable.chat.domain;

import com.ourdressingtable.common.util.MongoCreatedAtEntity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatrooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends MongoCreatedAtEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    private String name;

    private ChatroomType type;

    @Builder
    public Chatroom(String id, String name, ChatroomType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

}
