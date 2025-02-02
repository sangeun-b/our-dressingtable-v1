package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.SkinType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateMemberRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$)",
            message = "비밀번호는 알파벳 대/소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "별명을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    private Role role;

    private SkinType skinType;

    private ColorType colorType;
    private Date birthDate;

    private String imageUrl;

    @Builder
    public CreateMemberRequest(String email, String password, String name, String nickname, String phoneNumber, Role role, SkinType skinType, ColorType colorType, Date birthDate, String imageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.skinType = skinType;
        this.colorType = colorType;
        this.birthDate = birthDate;
        this.imageUrl = imageUrl;

    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .role(role)
                .skinType(skinType)
                .colorType(colorType)
                .birthDate(birthDate)
                .imageUrl(imageUrl)
                .build();

    }
}
