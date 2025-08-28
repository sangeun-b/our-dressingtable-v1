package com.ourdressingtable.membercosmetic.dto;

import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
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
public class MemberCosmeticResponse {

    @Schema(description = "회원 화장품 id", example="1")
    private Long id;

    @Schema(description = "화장품 브랜드", example = "이니스프리")
    private String brand;

    @Schema(description = "화장품 이름", example = "그린티 세럼")
    private String name;

    @Schema(description = "화장품 카테고리", example = "스킨>세럼")
    private String category;

    @Schema(description = "회원 화장품 이미지 url", example="https://image.img")
    private String imageUrl;


    @Builder
    public MemberCosmeticResponse(
            Long id,
            String brand,
            String name,
            String category,
            String imageUrl

    ) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public static MemberCosmeticResponse from(MemberCosmetic mc) {
        return MemberCosmeticResponse.builder()
                .id(mc.getId())
                .brand(mc.getBrand().getName())
                .name(mc.getName())
                .category(mc.getCategory().getName())
                .imageUrl(mc.getImageUrl())
                .build();
    }

}
