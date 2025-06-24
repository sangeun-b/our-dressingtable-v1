package com.ourdressingtable.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class FindEmailResponse {

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Builder
    public FindEmailResponse(String email) {
        this.email = email;
    }
}
