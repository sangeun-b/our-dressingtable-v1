package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.SkinType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    @Schema(description = "이메일", example = "sample@gmail.com")
    private String email;

    @Schema(description = "이름", example = "김이름")
    private String name;

    @Schema(description = "별명", example = "ME")
    private String nickname;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "피부타입", example = "NORMAL_SKIN")
    private SkinType skinType;

    @Schema(description = "퍼스널 컬러", example = "SPRING_WARM")
    private ColorType colorType;

    @Schema(description = "생년월일", example = "2025-08-12")
    private Date birthDate;

    @Schema(description = "회원 이미지", example = "URL")
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
