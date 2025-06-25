package com.ourdressingtable.auth.dto;

import com.ourdressingtable.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    @Schema(description = "발급된 accessToken", example = "accessToken")
    private String accessToken;

    @Schema(description = "발급된 refreshToken", example = "refreshToken")
    private String refreshToken;

    @Schema(description = "발급된 token type", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "로그인 된 회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "로그인 된 회원 EMAIL", example = "sample@gmail.com")
    private String email;

    @Schema(description = "로그인 된 회원 이름", example = "김이름")
    private String name;

    @Schema(description = "로그인 된 회원 닉네임", example = "닉네임네임")
    private String nickname;

    @Schema(description = "로그인 된 회원 프로필 이미지", example = "image_url")
    private String imageUrl;

    @Schema(description = "로그인 된 회원 ROLE", example = "ROLE_BASIC")
    private Role role;

    @Builder
    public LoginResponse(String accessToken, String refreshToken, Long memberId, String email, String name, String nickname, String imageUrl, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
    }
}
