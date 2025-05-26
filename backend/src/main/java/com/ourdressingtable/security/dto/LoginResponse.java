package com.ourdressingtable.security.dto;

import com.ourdressingtable.member.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String imageUrl;
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
