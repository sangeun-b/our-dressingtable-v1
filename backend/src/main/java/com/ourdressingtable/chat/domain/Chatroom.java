package com.ourdressingtable.chat.domain;

import com.ourdressingtable.common.util.CreatedAtEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatrooms")
public class Chatroom extends CreatedAtEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "chatroom_type", nullable = false)
    private ChatroomType type;

    @Builder
    public Chatroom(Long id, String name, ChatroomType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

}
