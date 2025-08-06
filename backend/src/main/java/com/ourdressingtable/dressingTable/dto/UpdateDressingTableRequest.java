package com.ourdressingtable.dressingtable.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateDressingTableRequest {

    @Schema(description = "수정할 화장대 이름", example = "new dt")
    private String name;

    @Schema(description = "화장대 이미지", example = "https://new-image.img")
    @URL
    private String imageUrl;

    @Builder
    public UpdateDressingTableRequest(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

}
