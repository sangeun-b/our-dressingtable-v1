package com.ourdressingtable.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {

    @Schema(description = "accesstoken", example = "accesstoken")
    private String accessToken;

    @Schema(description = "refreshtoken", example = "refreshtoken")
    private String refreshToken;

    @Builder
    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
