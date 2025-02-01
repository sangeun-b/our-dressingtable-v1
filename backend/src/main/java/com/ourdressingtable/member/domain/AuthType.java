package com.ourdressingtable.member.domain;

import lombok.Getter;

@Getter
public enum AuthType {
    KAKAO(0, "KAKAO"),
    NAVER(1, "NAVER"),
    GOOGLE(2, "GOOGLE"),
    APPLE(3, "APPLE"),
    GITHUB(4, "GITHUB");

    private final int code;
    private final String name;

    AuthType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
