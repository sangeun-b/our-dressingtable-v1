package com.ourdressingtable.notification.domain;

import com.ourdressingtable.common.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;

@Entity
@Table(name = "scheduled_notification",
    indexes = { @Index(name="idx_notify_at_status", columnList = "notifyAt,status")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduledNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(nullable = false)
    private Instant notifyAt;

    @Column(nullable = false)
    private String timezone;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    public long epochMillis() {
        return notifyAt.toEpochMilli();
    }

    public ZoneId zoneId() {
        return ZoneId.of(timezone);
    }

    @Builder
    public ScheduledNotification(Long id, Long memberId, NotificationType type, String payloadJson, Instant notifyAt, String timezone, NotificationStatus status) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.payloadJson = payloadJson;
        this.notifyAt = notifyAt;
        this.timezone = timezone;
        this.status = status;

    }

    public void markEnqueued() {
        this.status = NotificationStatus.ENQUEUED;
    }

    public void marksent() {
        this.status = NotificationStatus.SENT;
    }

    public void cancel() {
        this.status = NotificationStatus.CANCELLED;
    }
}

