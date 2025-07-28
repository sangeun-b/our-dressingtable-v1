package com.ourdressingtable.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "1:1 채팅방 생성 요청 DTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOneToOneChatRequest {

    @Schema(description = "상대방 회원 ID", example = "2")
    @NotNull(message = "대상 회원 ID는 필수입니다.")
    private String targetMemberId;


}
