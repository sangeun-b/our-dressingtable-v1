package com.ourdressingtable.community.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryResponse {

    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "게시글 제목", example = "이 화장품 어때요?")
    private String title;

    @Schema(description = "게시글 카테고리 이름", example = "후기")
    private String categoryName;

    @Schema(description = "게시글 조회수", example = "123")
    private int viewCount;

    @Schema(description = "게시글 작성자", example = "사용자1")
    private String memberName;

    @Schema(description = "게시글 생성일", example = "2025년 3월 1일 13:25")
    private Timestamp createdAt;
}
