package com.ourdressingtable.community.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentRequest {

    @Schema(description = "댓글 내용", example = "공감이에요")
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    @Schema(description = "게시글  ID", example = "1")
    @NotNull
    private Long postId;

    @Schema(description = "부모 댓글 ID(대댓글인 경우)", example = "2")
    private Long parentId;

    @Builder
    public CreateCommentRequest(String content, Long postId, Long parentId) {
        this.content = content;
        this.postId = postId;
        this.parentId = parentId;
    }


}
