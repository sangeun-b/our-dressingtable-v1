package com.ourdressingtable.auth.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendEmailVerificationCodeRequest {

    @Schema(description = "이메일", example = "sample@gmail.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Builder
    public SendEmailVerificationCodeRequest(String email) {
        this.email = email;
    }
}
