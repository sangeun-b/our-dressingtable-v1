package com.ourdressingtable.membercosmetic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberCosmeticResponse {

    @Schema(description = "생성된 화장품 ID", example = "1")
    private Long id;

}
