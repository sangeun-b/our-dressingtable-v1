package com.ourdressingtable.chat.dto;

import com.ourdressingtable.chat.domain.Chat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "1:1 채팅방 요약 응답 DTO")
public record OneToOneChatroomSummaryResponse (
    @Schema(description = "채팅방 ID", example = "1")
    String chatroomId,

    @Schema(description = "채팅 상대 ID", example = "1")
    String targetMemberId,

    @Schema(description = "채팅 상대 닉네임", example = "you")
    String targetNickname,

    @Schema(description = "채팅 상대 프로필 이미지 url", example = "URL")
    String targetProfileImageUrl,

    @Schema(description = "마지막 메시지", example = "고마워!")
    String lastMessage,

    @Schema(description = "마지막 채팅 시간", example = "2025-05-12T13:20:00")
    LocalDateTime lastMessageTime,

    @Schema(description = "읽지 않은 메세지 수", example = "13")
    long unreadCount
    ){
}
