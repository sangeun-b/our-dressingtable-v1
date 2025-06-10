package com.ourdressingtable.dressingTable.dto;

import com.ourdressingtable.dressingTable.domain.DressingTable;
import com.ourdressingtable.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateDressingTableRequest {

    @Schema(description = "수정할 화장대 이름", example = "new dt")
    private String name;

    @Schema(description = "화장대 이미지", example = "image_url")
    @URL
    private String imageUrl;

    @Builder
    public UpdateDressingTableRequest(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

}
