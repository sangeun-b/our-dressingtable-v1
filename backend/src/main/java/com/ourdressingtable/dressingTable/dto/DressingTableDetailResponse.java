package com.ourdressingtable.dressingtable.dto;

import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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
public class DressingTableDetailResponse {

    @Schema(description = "화장대 id", example = "1")
    private Long id;

    @Schema(description = "화장대 이름", example = "my dt")
    private String name;

    @Schema(description = "화장대 이미지", example = "https://image.img")
    private String imageUrl;

    @Schema(description = "등록된 화장품")
    private List<MemberCosmetic> memberCosmetics;

    @Builder
    public DressingTableDetailResponse(Long id, String name, String imageUrl, List<MemberCosmetic> memberCosmetics) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.memberCosmetics = memberCosmetics;
    }


    public static DressingTableDetailResponse from(DressingTable dressingTable) {
        return DressingTableDetailResponse.builder()
                .id(dressingTable.getId())
                .name(dressingTable.getName())
                .imageUrl(dressingTable.getImageUrl())
                .memberCosmetics(dressingTable.getMemberCosmetics())
                .build();
    }
}
