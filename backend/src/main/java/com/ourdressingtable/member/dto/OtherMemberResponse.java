package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.SkinType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OtherMemberResponse {

    private String nickname;

    private SkinType skinType;

    private ColorType colorType;

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
