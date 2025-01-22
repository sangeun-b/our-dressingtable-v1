package com.ourdressingtable.domain.member.domain;

import lombok.Getter;

@Getter
public enum SkinType {
    NORMAL_SKIN(0, "중성"),
    DRY_SKIN(1, "건성"),
    OILY_SKIN(2,"지성"),
    COMBINATION_SKIN(3,"복합성"),
    SENSITIVE_SKIN(4,"민감성");

    private final int code;
    private final String name;

    SkinType(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
