package com.ourdressingtable.auth.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmEmailVerificationCodeRequest {

    @Schema(description = "이메일", example = "sample@gmail.com", required = true)
    @Email
    @NotBlank
    private String email;

    @Schema(description = "인증코드", example = "123456")
    @NotBlank
    private String verificationCode;

    @Builder
    public ConfirmEmailVerificationCodeRequest(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
