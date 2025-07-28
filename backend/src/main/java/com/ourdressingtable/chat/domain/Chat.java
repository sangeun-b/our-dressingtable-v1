package com.ourdressingtable.chat.domain;


import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "chats",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"chatroom_id","member_id"})
//)
public class  Chat {

    @Id
    private String id = UUID.randomUUID().toString();

    private LocalDateTime joinAt;

    private boolean isActive = true;

    private String chatroomId;

    private String memberId;

    @Builder
    public Chat(String id, LocalDateTime joinAt, boolean isActive, String chatroomId, String memberId) {
        this.id = id;
        this.joinAt = joinAt;
        this.isActive = isActive;
        this.chatroomId = chatroomId;
        this.memberId = memberId;
    }

    public void updateActive(boolean active) {
        this.isActive = active;
    }

}
