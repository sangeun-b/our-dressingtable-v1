package com.ourdressingtable.chat.component;

import com.ourdressingtable.chat.dto.ChatMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaChatConsumer {

    @KafkaListener(topics = "chat-message", groupId = "chat-consumer")
    public void consume(ChatMessage message) {
        System.out.println("[Kafka 수신] " + message);
    }
}
