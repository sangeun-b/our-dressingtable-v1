package com.ourdressingtable.chat.domain;

import com.ourdressingtable.member.domain.Member;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_reads")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "chat_reads", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"chatroom_id","member_id"})
//})
public class ChatRead {

    @Id
    private String id;

    private String chatroomId;

    private String memberId;

    private LocalDateTime lastReadAt;

    @Builder
    public ChatRead(String chatroomId, String memberId, LocalDateTime lastReadAt) {
        this.chatroomId = chatroomId;
        this.memberId = memberId;
        this.lastReadAt = lastReadAt;
    }

    public void updateReadAt(LocalDateTime lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
}
