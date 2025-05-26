package com.ourdressingtable.community.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePostRequest {

    @Schema(description = "게시글 제목", example = "추천 화장품")
    private String title;
    @Schema(description = "게시글 내용", example = "화장품 추천해주세요.")
    private String content;
    @Schema(description = "게시글 카테고리 ID", example = "1")
    private Long communityCategoryId;
    @Schema(description = "게시글 첨부 이미지", example = "이미지 URL")
    private List<String> images;

    @Builder
    public UpdatePostRequest(String title, String content, Long communityCategoryId, List<String> images) {
        this.title = title;
        this.content = content;
        this.communityCategoryId = communityCategoryId;
        this.images = images;
    }
}
