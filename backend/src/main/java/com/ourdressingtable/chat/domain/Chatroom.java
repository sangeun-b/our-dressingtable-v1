package com.ourdressingtable.chat.domain;

import com.ourdressingtable.common.util.CreatedAtEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatrooms")
@SQLRestriction("is_deleted = false")
public class Chatroom extends CreatedAtEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "chatroom_type", nullable = false)
    private ChatroomType type;

    @Column(name="is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @Builder
    public Chatroom(Long id, String name, ChatroomType type, boolean isDeleted, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
