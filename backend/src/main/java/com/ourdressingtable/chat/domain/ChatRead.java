package com.ourdressingtable.chat.domain;

import com.ourdressingtable.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_reads", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"chatroom_id","member_id"})
})
public class ChatRead {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_read_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "last_read_at", nullable = false)
    private LocalDateTime lastReadAt;

    @Builder
    public ChatRead(Chatroom chatroom, Member member, LocalDateTime lastReadAt) {
        this.chatroom = chatroom;
        this.member = member;
        this.lastReadAt = lastReadAt;
    }

    public void updateReadAt(LocalDateTime lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
}
