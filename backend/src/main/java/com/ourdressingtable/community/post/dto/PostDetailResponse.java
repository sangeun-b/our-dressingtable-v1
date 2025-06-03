package com.ourdressingtable.community.post.dto;

import com.ourdressingtable.community.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;

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

    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "게시글 제목", example = "이 화장품 어때요?")
    private String title;

    @Schema(description = "게시글 내용", example = "사용해보신 분 후기 알려주세요.")
    private String content;

    @Schema(description = "게시글 카테고리 이름", example = "후기")
    private String categoryName;

    @Schema(description = "게시글 조회수", example = "123")
    private int viewCount;

    @Schema(description = "게시글 좋아요", example = "345")
    private int postLikes;

    @Schema(description = "로그인 사용자가 좋아요를 눌렀는지 여부", example = "true")
    private boolean likedByCurrentMember;

    @Schema(description = "게시글 작성자", example = "사용자1")
    private String memberName;

    @Schema(description = "게시글 생성일", example = "2025년 3월 1일 13:25")
    private Timestamp createdAt;

    @Builder
    public PostDetailResponse(Long id, String title, String content, String categoryName, int viewCount, int postLikes, boolean likedByCurrentMember, String memberName, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.viewCount = viewCount;
        this.postLikes = postLikes;
        this.likedByCurrentMember = likedByCurrentMember;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }

    public static PostDetailResponse from(Post post, boolean likedByCurrentMember) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCommunityCategory().getName())
                .viewCount(post.getViewCount())
                .postLikes(post.getLikeCount())
                .likedByCurrentMember(likedByCurrentMember)
                .memberName(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
