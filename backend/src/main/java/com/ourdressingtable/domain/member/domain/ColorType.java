package com.ourdressingtable.domain.member.domain;

import lombok.Getter;

@Getter
public enum ColorType {
    SPRING_WARM(0, "봄 웜"),
    SUMMER_COOL(1,"여름 쿨"),
    AUTUMN_WARM(2, "가을 웜"),
    WINTER_COOL(3, "겨울 쿨");

    private final int code;
    private final String name;

    ColorType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
