package com.ourdressingtable.notification.inbox;

import com.ourdressingtable.notification.domain.NotificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationInbox {

    @Id
    private String id;

    private Long notificationId;

    private Long memberId;

    private String title;

    private String body;

    private String type;

    private String dataJson;

    private boolean read;

    @Builder
    public NotificationInbox(String id, Long notificationId, Long memberId, String title, String body, String type, String dataJson, boolean read) {
        this.id = id;
        this.notificationId = notificationId;
        this.memberId = memberId;
        this.title = title;
        this.body = body;
        this.type = type;
        this.dataJson = dataJson;
        this.read = read;
    }
}
