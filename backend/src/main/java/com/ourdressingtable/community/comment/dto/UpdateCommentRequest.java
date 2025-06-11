package com.ourdressingtable.community.comment.dto;

import com.ourdressingtable.community.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateCommentRequest {

    @Schema(description = "댓글 내용", example = "수정했어요!")
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    @Builder
    public UpdateCommentRequest(String content) {
        this.content = content;
    }

}
