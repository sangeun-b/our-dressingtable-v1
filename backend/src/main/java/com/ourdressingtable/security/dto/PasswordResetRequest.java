package com.ourdressingtable.security.dto;

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
public class PasswordResetRequest {

    @Schema(description = "이메일", example = "sample@gmail.com")
    @NotBlank(message = "이메일을 입려해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;


    @Schema(description = "새 비밀번호", example = "passwordme!!")
    @NotBlank(message = "새로운 비밀번호를 입려해주세요.")
    private String newPassword;

    @Builder
    public PasswordResetRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
}
