package com.ourdressingtable.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ResetPasswordEmailRequest {

    @Schema(description = "이메일", example = "sample@gmail.com")
    @NotBlank(message = "이메일을 입려해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Builder
    public ResetPasswordEmailRequest(String email) {
        this.email = email;
    }
}
