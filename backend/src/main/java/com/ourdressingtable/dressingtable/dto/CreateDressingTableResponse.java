package com.ourdressingtable.dressingtable.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateDressingTableResponse {

    @Schema(description = "생성된 화장대 ID", example = "1")
    private Long id;
}
