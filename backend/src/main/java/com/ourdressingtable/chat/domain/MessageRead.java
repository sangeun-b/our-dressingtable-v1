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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "message_reads",
        uniqueConstraints = @UniqueConstraint(columnNames = {"message_id","member_id"})
)
public class MessageRead {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_read_id")
    private Long id;

    @Column(name = "is_read")
    @ColumnDefault("false")
    private boolean isRead = false;

    @Column(name = "read_at")
    private Timestamp readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public MessageRead(boolean isRead, Timestamp readAt, Message message, Member member) {
        this.isRead = isRead;
        this.readAt = readAt;
        this.message = message;
        this.member = member;
    }
}
