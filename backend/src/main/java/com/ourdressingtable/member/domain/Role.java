package com.ourdressingtable.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_BASIC(0,"ROLE_BASIC", "일반회원"),
    ROLE_PRO(1, "ROLE_PRO", "유료회원"),
    ROLE_ADMIN(1,"ROLE_ADMIN", "관리자");

    private final int code;
    private final String auth;
    private final String desc;

    Role(int code, String auth, String desc) {
        this.code = code;
        this.auth = auth;
        this.desc = desc;
    }
}
