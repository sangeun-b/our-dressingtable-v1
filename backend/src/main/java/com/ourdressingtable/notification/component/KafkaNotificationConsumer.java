package com.ourdressingtable.notification.component;

import com.ourdressingtable.notification.repository.ScheduledNotificationRepository;
import com.ourdressingtable.notification.service.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationConsumer {
    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final NotificationSenderService sender;

    @KafkaListener(topics = "notifications", groupId = "notification-sender")
    public void listen(String notificationId) {
        long id = Long.parseLong(notificationId);
        scheduledNotificationRepository.findById(id).ifPresent(sender::deliver);
    }
}
