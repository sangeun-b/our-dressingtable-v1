package com.ourdressingtable.chat.domain;

import com.ourdressingtable.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chatroom_id","member_id"})
)
public class Chat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "join_at", nullable = false)
    private Timestamp joinAt;

    @Column(name = "is_active")
    @ColumnDefault("true")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Chat(Long id, Timestamp joinAt, boolean isActive, Chatroom chatroom, Member member) {
        this.id = id;
        this.joinAt = joinAt;
        this.isActive = isActive;
        this.chatroom = chatroom;
        this.member = member;
    }

    public void updateActive(boolean active) {
        this.isActive = active;
    }

}
