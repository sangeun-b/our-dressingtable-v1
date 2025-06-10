package com.ourdressingtable.community.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostSearchCondition {

    @Schema(description = "검색 대상 필드", example = "title | cotent | title_content | author")
    private String searchType;

    @Schema(description = "검색 키워드", example = "검색어를 입력해주세요.")
    private String keyword;

    @Schema(description = "카테고리 분류 필터", example = "카테고리를 입력해주세요.")
    private String category;

    @Schema(description = "정렬 기준", example = "latest | likes | views")
    private String sortBy;

    @Builder
    public PostSearchCondition(String searchType, String keyword, String category, String sortBy) {
        this.searchType = searchType;
        this.keyword = keyword;
        this.category = category;
        this.sortBy = sortBy;
    }
}
