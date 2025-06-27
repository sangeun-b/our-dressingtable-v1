package com.ourdressingtable.chat.controller;

import com.ourdressingtable.chat.dto.ChatMessage;
import com.ourdressingtable.chat.service.KafkaChatProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name = "채팅", description = "채팅 API")
public class ChatController {

    private final KafkaChatProducer kafkaChatProducer;

    @Operation(summary = "메시지 전송", description = "사용자가 메시지를 전송합니다.")
    @PostMapping()
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid ChatMessage message) {
        kafkaChatProducer.sendMessage("chat-message", message);
        return ResponseEntity.ok().build();
    }
}
