package com.ourdressingtable.membercosmetic.domain;

import lombok.Getter;

@Getter
public enum NotifyType {
    EXPIRED(0,"EXPIRED","유통기한"),
    OPEN(1,"OPEN","오픈날짜"),
    USE(2,"USE","사용기한");

    private final int code;
    private final String name;
    private final String desc;

    NotifyType(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }
}
