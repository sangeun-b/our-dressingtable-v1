package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.dto.ChatMessageRequest;
import com.ourdressingtable.common.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Kafka - 메시지 전송 테스트")
public class KafkaChatProducerTest {

    @Mock
    private KafkaTemplate<String , ChatMessageRequest> kafkaTemplate;

    @InjectMocks
    private KafkaChatProducer kafkaChatProducer;

    @Test
    public void sendMessage_shouldCallKafkaChatProducer() {

        String topic = "chat-message";
        ChatMessageRequest chatMessageRequest = TestDataFactory.testChatMessageRequest("1","1");

        kafkaChatProducer.sendMessage(topic, chatMessageRequest);

        verify(kafkaTemplate).send(topic,chatMessageRequest);

    }

}
