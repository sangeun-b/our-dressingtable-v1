package com.ourdressingtable.community.post.domain;

public enum PostSortType {
    LATEST, LIKES, VIEWS;

    public static PostSortType fromString(String value) {
        if(value == null) return LATEST;
        try {
            return PostSortType.valueOf(value.toUpperCase());
        } catch(IllegalArgumentException e) {
            return LATEST;
        }
    }
}
