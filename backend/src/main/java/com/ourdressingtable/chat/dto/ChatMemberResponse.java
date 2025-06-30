package com.ourdressingtable.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "채팅방 참여 회원 응답 DTO")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMemberResponse {

    @Schema(description = "회원 ID", example = "101")
    private Long memberId;

    @Schema(description = "채팅방 참여 시각")
    private LocalDateTime joinAt;

    @Builder
    public ChatMemberResponse(Long memberId, LocalDateTime joinAt) {
        this.memberId = memberId;
        this.joinAt = joinAt;
    }

}
