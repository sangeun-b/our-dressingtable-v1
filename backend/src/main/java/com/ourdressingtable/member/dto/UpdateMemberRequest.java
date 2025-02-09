package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.SkinType;
import jakarta.validation.constraints.Pattern;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberRequest {

    // TODO: Passay or CUSTOM ANNOTATION 생성 후 처리 하기
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 알파벳 대/소문자, 숫자, 특수문자가 포함한 8자~20자 사이여야 합니다.")
    private String password;

    private String nickname;

    private String phoneNumber;

    private SkinType skinType;

    private ColorType colorType;

    private Date birthDate;

    private String imageUrl;

    @Builder
    public UpdateMemberRequest(String password, String nickname, String phoneNumber, SkinType skinType, ColorType colorType, Date birthDate, String imageUrl) {
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.skinType = skinType;
        this.colorType = colorType;
        this.birthDate = birthDate;
        this.imageUrl = imageUrl;
    }

    public Member toEntity() {
        return Member.builder()
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .skinType(skinType)
                .colorType(colorType)
                .birthDate(birthDate)
                .imageUrl(imageUrl)
                .build();
    }

}
