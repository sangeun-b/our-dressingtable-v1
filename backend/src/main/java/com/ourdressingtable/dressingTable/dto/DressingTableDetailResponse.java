package com.ourdressingtable.dressingtable.dto;

import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.dto.MemberCosmeticResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
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
    private List<MemberCosmeticResponse> memberCosmetics;

    @Builder
    public DressingTableDetailResponse(Long id, String name, String imageUrl, List<MemberCosmeticResponse> memberCosmetics) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.memberCosmetics = memberCosmetics;
    }


    public static DressingTableDetailResponse from(DressingTable dressingTable) {
        List<MemberCosmeticResponse> memberCosmeticResponses = dressingTable.getMemberCosmetics()
                .stream().map(MemberCosmeticResponse::from)
                .collect(Collectors.toList());

        return DressingTableDetailResponse.builder()
                .id(dressingTable.getId())
                .name(dressingTable.getName())
                .imageUrl(dressingTable.getImageUrl())
                .memberCosmetics(memberCosmeticResponses)
                .build();
    }
}
