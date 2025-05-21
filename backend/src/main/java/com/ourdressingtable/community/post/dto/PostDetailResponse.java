package com.ourdressingtable.community.post.dto;

import com.ourdressingtable.community.post.domain.Post;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostDetailResponse {

    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private int viewCount;
    private String memberName;
    private Timestamp createdAt;

    @Builder
    public PostDetailResponse(Long id, String title, String content, String categoryName, String images, int viewCount, String memberName, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.viewCount = viewCount;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }

    public static PostDetailResponse from(Post post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCommunityCategory().getName())
                .viewCount(post.getViewCount())
                .memberName(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
