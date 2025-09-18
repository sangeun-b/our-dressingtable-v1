package com.ourdressingtable.notification.service;

import com.ourdressingtable.notification.domain.ScheduledNotification;
import com.ourdressingtable.notification.inbox.NotificationInbox;
import com.ourdressingtable.notification.repository.ScheduledNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationSenderSerivceImpl implements NotificationSenderService {
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ScheduledNotificationRepository scheduledNotificationRepository;

    @Override
    @Transactional
    public void deliver(ScheduledNotification scheduledNotification) {
        String title = switch (scheduledNotification.getType()) {
            case COSMETIC_EXPIRY_MONTH_BEFORE -> "[유통기한 D-30] 유통기한 한 달 남았습니다.";
            case COSMETIC_EXPIRY_WEEK_BEFORE -> "[유통기한 D-30] 유통기한 일주일 남았습니다.";
            case COSMETIC_EXPIRY_DAY_BEFORE -> "[유통기한 D-30] 유통기한 하루 남았습니다.";
            case COSMETIC_EXPIRY_DAY_OF -> "[유통기한 D-30] 유통기한 당일 입니다.";
        };

        var inbox = NotificationInbox.builder()
                .notificationId(scheduledNotification.getId())
                .title(title)
                .body("상세 보기에서 확인하세요")
                .type(scheduledNotification.getType().name())
                .dataJson(scheduledNotification.getPayloadJson())
                .read(false)
                .build();

        mongoTemplate.save(inbox);
        simpMessagingTemplate.convertAndSend("/topic/notification" + scheduledNotification.getId(), inbox);

        scheduledNotification.marksent();
        scheduledNotificationRepository.save(scheduledNotification);
    }
}
