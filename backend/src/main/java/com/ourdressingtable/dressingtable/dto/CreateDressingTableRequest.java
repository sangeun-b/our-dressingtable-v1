package com.ourdressingtable.dressingtable.dto;

import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDressingTableRequest {

    @Schema(description = "화장대 이름", example = "my dt", required = true)
    @NotBlank
    private String name;

    @Schema(description = "화장대 이미지", example = "https://image.img")
    @URL
    private String imageUrl;

    @Builder
    public CreateDressingTableRequest(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public DressingTable toEntity(Member member) {
        return DressingTable.builder()
                .name(name)
                .imageUrl(imageUrl)
                .member(member)
                .build();

    }
}
