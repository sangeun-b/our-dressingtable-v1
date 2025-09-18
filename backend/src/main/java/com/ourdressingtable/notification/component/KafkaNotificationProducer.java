package com.ourdressingtable.notification.component;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void send(String notificationId) {
        kafkaTemplate.send("notification", notificationId);
    }
}
