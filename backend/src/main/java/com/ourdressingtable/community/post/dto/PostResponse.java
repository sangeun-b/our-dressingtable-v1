package com.ourdressingtable.community.post.dto;

import com.ourdressingtable.community.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "게시글 응답 DTO")
public class PostResponse {


    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "게시글 제목", example = "이 화장품 어때요?")
    private String title;

    @Schema(description = "게시글 카테고리 이름", example = "후기")
    private String categoryName;

    @Schema(description = "게시글 조회수", example = "123")
    private int viewCount;

    @Schema(description = "게시글 좋아요", example = "345")
    private int likeCount;

    @Schema(description = "게시글 작성자", example = "사용자1")
    private String author;

    @Schema(description = "게시글 생성일", example = "2025년 3월 1일 13:25")
    private LocalDateTime createdAt;

    @Builder
    public PostResponse(Long id, String title, String categoryName, int viewCount, int likeCount, String author, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.categoryName = categoryName;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.author = author;
        this.createdAt = createdAt;
    }

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .categoryName(post.getCommunityCategory().getName())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .author(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .build();
    }


}
