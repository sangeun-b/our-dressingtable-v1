package com.ourdressingtable.chat.controller;

import com.ourdressingtable.chat.service.ChatReadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
@Tag(name = "메시지 읽음", description = "메시지 읽음 API")
public class ChatReadController {

    private final ChatReadService chatReadService;

    @PatchMapping("/{chatroomId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long chatroomId) {
        chatReadService.markAsRead(chatroomId);
        return ResponseEntity.ok().build();
    }

}
