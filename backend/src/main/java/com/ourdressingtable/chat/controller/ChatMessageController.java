package com.ourdressingtable.chat.controller;

import com.ourdressingtable.chat.dto.ChatMessageRequest;
import com.ourdressingtable.chat.service.ChatReadService;
import com.ourdressingtable.chat.service.KafkaChatProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "채팅 메시지", description = "채팅 메시지 관련 API")
public class ChatMessageController {

    private final KafkaChatProducer kafkaChatProducer;

    private final ChatReadService chatReadService;


    @Operation(summary = "메시지 전송", description = "사용자가 메시지를 전송합니다.")
    @PostMapping("/api/chatrooms/{chatroomId}/messages")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid ChatMessageRequest message) {
        kafkaChatProducer.sendMessage("chat-message", message);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메시지 읽음 처리", description = "메시지를 읽음 처리합니다.")
    @PatchMapping("/api/chatrooms/{chatroomId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String chatroomId) {
        chatReadService.markAsRead(chatroomId);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/ws/chat/send")
    public void sendMessageByWebsocket(ChatMessageRequest chatMessage) {
        kafkaChatProducer.sendMessage("chat-message", chatMessage);
    }

}