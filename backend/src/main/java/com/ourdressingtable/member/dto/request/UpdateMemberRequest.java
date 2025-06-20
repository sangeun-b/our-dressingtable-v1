package com.ourdressingtable.member.dto.request;

import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.SkinType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "비밀번호", example = "Password123!@@@@@@@?")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 알파벳 대/소문자, 숫자, 특수문자가 포함한 8자~20자 사이여야 합니다.")
    private String password;

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
