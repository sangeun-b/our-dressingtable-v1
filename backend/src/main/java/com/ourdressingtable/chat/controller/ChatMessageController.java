package com.ourdressingtable.chat.controller;

import com.ourdressingtable.chat.dto.ChatMessage;
import com.ourdressingtable.chat.service.KafkaChatProducer;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "채팅 메시지", description = "채팅 메시지 관련 API")
public class ChatMessageController {

    private final KafkaChatProducer kafkaChatProducer;

    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessage chatMessage) {
        kafkaChatProducer.sendMessage("chat-topic", chatMessage);
    }



}
