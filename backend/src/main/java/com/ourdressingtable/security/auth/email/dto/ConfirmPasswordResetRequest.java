package com.ourdressingtable.security.auth.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ConfirmPasswordResetRequest {

    @Schema(description = "이메일", example = "sample@example.com")
    @NotBlank(message = "토큰을 입력해주세요.")
    private String token;


    @Schema(description = "새 비밀번호", example = "passwordme!!")
    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String newPassword;

    @Builder
    public ConfirmPasswordResetRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }
}
