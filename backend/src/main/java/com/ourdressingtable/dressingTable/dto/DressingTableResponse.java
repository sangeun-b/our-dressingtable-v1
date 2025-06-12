package com.ourdressingtable.dressingTable.dto;

import com.ourdressingtable.dressingTable.domain.DressingTable;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class DressingTableResponse {

    @Schema(description = "화장대 id", example = "1")
    private Long id;

    @Schema(description = "화장대 이름", example = "my dt")
    private String name;

    @Schema(description = "화장대 이미지", example = "https://image.img")
    private String imageUrl;

    @Builder
    public DressingTableResponse(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static DressingTableResponse from(DressingTable dressingTable) {
        return DressingTableResponse.builder()
                .id(dressingTable.getId())
                .name(dressingTable.getName())
                .imageUrl(dressingTable.getImageUrl())
                .build();
    }

}
