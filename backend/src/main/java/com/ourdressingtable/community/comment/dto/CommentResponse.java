package com.ourdressingtable.community.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CommentResponse {

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "댓글 내용", example = "공감입니다.")
    private String content;

    @Schema(description = "대댓글인 경우", example = "1")
    private int depth;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    @Schema(description = "부모 댓글 ID(대댓글인 경우)", example = "1")
    private Long parentId;

    @Builder
    public CommentResponse (Long commentId, String content, int depth, Long memberId, Long postId, Long parentId) {
        this.commentId = commentId;
        this.content = content;
        this.depth = depth;
        this.memberId = memberId;
        this.postId = postId;
        this.parentId = parentId;
    }
}
