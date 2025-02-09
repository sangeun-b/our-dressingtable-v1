package com.ourdressingtable.member.domain;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVATE(0, "활성화", "활성화 중인 계정"),
    LOCK(1, "일시 중지", "일시 중지 중인 계정"),
    BLOCK(2, "영구 중지", "영구 중지 중인 계정"),
    WITHDRAWAL(3, "탈퇴", "탈퇴한 계정");

    private final int code;
    private final String name;
    private final String description;

    Status(final int code, final String name, final String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }


}


