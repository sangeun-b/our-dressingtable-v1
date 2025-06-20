package com.ourdressingtable.member.dto.response;

import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.SkinType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OtherMemberResponse {

    @Schema(description = "별명", example = "ME")
    private String nickname;

    @Schema(description = "피부타입", example = "NORMAL_SKIN")
    private SkinType skinType;

    @Schema(description = "퍼스널 컬러", example = "SPRING_WARM")
    private ColorType colorType;

    @Schema(description = "회원 이미지", example = "URL")
    private String imageUrl;

    @Builder
    public OtherMemberResponse(String email, String name, String nickname, String phoneNumber, SkinType skinType, ColorType colorType, Date birthDate, String imageUrl) {
        this.nickname = nickname;
        this.skinType = skinType;
        this.colorType = colorType;
        this.imageUrl = imageUrl;
    }

    public static OtherMemberResponse fromEntity(Member member) {
        return OtherMemberResponse.builder()
                .nickname(member.getNickname())
                .skinType(member.getSkinType())
                .colorType(member.getColorType())
                .imageUrl(member.getImageUrl())
                .build();
    }


}
