package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.dto.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaChatProducer {

    private final KafkaTemplate<String, ChatMessageRequest> kafkaTemplate;

    public void sendMessage(String topic, ChatMessageRequest message) {
        kafkaTemplate.send(topic, message);
    }

}
