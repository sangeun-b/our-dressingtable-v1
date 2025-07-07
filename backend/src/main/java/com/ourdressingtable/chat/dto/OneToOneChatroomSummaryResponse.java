package com.ourdressingtable.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "1:1 채팅방 요약 응답 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneToOneChatroomSummaryResponse {
    @Schema(description = "채팅방 ID", example = "1")
    private Long chatroomId;

    @Schema(description = "채팅 상대 ID", example = "1")
    private Long targetMemberId;

    @Schema(description = "채팅 상대 닉네임", example = "you")
    private String targetNickname;

    @Schema(description = "채팅 상대 프로필 이미지 url", example = "URL")
    private String targetProfileImageUrl;

    @Schema(description = "마지막 메시지", example = "고마워!")
    private String lastMessage;

    @Schema(description = "마지막 채팅 시간", example = "2025년 05월 12일 13:20")
    private LocalDateTime lastMessageTime;

    @Schema(description = "읽지 않은 메시지 수", example = "10")
    private long unreadCount;

    @Builder
    public OneToOneChatroomSummaryResponse(Long chatroomId, Long targetMemberId, String targetNickname, String targetProfileImageUrl, String lastMessage, LocalDateTime lastMessageTime, long unreadCount) {
        this.chatroomId = chatroomId;
        this.targetMemberId = targetMemberId;
        this.targetNickname = targetNickname;
        this.targetProfileImageUrl = targetProfileImageUrl;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
    }

}
