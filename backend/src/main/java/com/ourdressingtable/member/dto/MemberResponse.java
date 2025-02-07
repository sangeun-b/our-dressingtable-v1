package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.SkinType;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    private String email;

    private String name;

    private String nickname;

    private String phoneNumber;

    private SkinType skinType;

    private ColorType colorType;

    private Date birthDate;

    private String imageUrl;

    @Builder
    public MemberResponse(String email, String name, String nickname, String phoneNumber, SkinType skinType, ColorType colorType, Date birthDate, String imageUrl) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.skinType = skinType;
        this.colorType = colorType;
        this.birthDate = birthDate;
        this.imageUrl = imageUrl;
    }

    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .skinType(member.getSkinType())
                .colorType(member.getColorType())
                .birthDate(member.getBirthDate())
                .imageUrl(member.getImageUrl())
                .build();
    }

}
