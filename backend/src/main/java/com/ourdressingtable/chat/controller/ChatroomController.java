package com.ourdressingtable.chat.controller;

import com.ourdressingtable.chat.dto.ChatMemberResponse;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import com.ourdressingtable.chat.dto.CreateChatroomRequest;
import com.ourdressingtable.chat.dto.CreateChatroomResponse;
import com.ourdressingtable.chat.dto.CreateOneToOneChatRequest;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;
import com.ourdressingtable.chat.service.ChatroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
@Tag(name = "채팅방", description = "채팅방 API")
public class ChatroomController {

    private final ChatroomService chatroomService;

    @PostMapping
    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
    public ResponseEntity<CreateChatroomResponse> createChatroom(@Valid @RequestBody CreateChatroomRequest request) {
        Long id = chatroomService.createChatroom(request);
        return ResponseEntity.created(URI.create("/api/chatrooms/"+id))
                .body(new CreateChatroomResponse(id));
    }

    @PostMapping("/{chatroomId}/members")
    @Operation(summary = "채팅방 참여", description = "채팅방에 참여합니다.")
    public ResponseEntity<Void> join(@PathVariable Long chatroomId) {
        chatroomService.joinChatroom(chatroomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatroomId}/members")
    @Operation(summary = "채팅방 나가기", description = "채팅방에서 나가기를 합니다.")
    public ResponseEntity<Void> leave(@PathVariable Long chatroomId) {
        chatroomService.leaveChatroom(chatroomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chatroomId}/members")
    @Operation(summary = "채팅방 참여 회원", description = "채팅방에 참여한 회원들을 조회합니다.")
    public ResponseEntity<List<ChatMemberResponse>> getMembers(@PathVariable Long chatroomId) {
        return ResponseEntity.ok(chatroomService.getActiveChatMembers(chatroomId));
    }

    @PostMapping("/one-to-one")
    @Operation(summary = "1:1 채팅방 참여", description = "1:1 채팅방에 참여합니다.")
    public ResponseEntity<ChatroomResponse> createChatMember(@Valid @RequestBody CreateOneToOneChatRequest request) {
        return ResponseEntity.ok(chatroomService.createOrGetOneToOneChatroom(
                request.getTargetMemberId()));
    }

    @GetMapping("/one-to-one/my-chatrooms")
    @Operation(summary = "1:1 채팅방 목록", description = "내가 참여 중인 1:1 채팅방 목록을 조회합니다.")
    public ResponseEntity<List<OneToOneChatroomSummaryResponse>> getMyOneToOneChatrooms() {
        return ResponseEntity.ok(chatroomService.getMyOneToOneChatrooms());
    }

}
