package com.ourdressingtable.community.post.util;

import com.ourdressingtable.community.post.domain.QPost;
import com.querydsl.core.types.OrderSpecifier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSortUtil {

    public static OrderSpecifier<?> getOrderSpecifier(String sortBy, QPost post) {
        if (sortBy == null || sortBy.isBlank()) {
           return post.createdAt.desc();
        }

        return switch (sortBy.toLowerCase()) {
            case "likes" -> post.likeCount.desc();
            case "views" -> post.viewCount.desc();
            case "latest" -> post.createdAt.desc();
            default -> post.createdAt.desc();
        };
    }
}
