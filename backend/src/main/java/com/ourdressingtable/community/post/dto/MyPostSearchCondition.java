package com.ourdressingtable.community.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MyPostSearchCondition {

    @Schema(description = "정렬 기준", example = "latest | likes | views")
    private String sortBy;

    @Schema(description = "날짜 필터")
    private LocalDate from;

    @Builder
    public MyPostSearchCondition(String category, String sortBy, LocalDate from) {
        this.sortBy = sortBy;
        this.from = from;
    }
}
