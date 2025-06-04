package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.WithdrawalMember;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalMemberResponse {

    @Schema(description = "탈퇴 회원 ID", example = "1")
    private Long id;

    @Schema(description = "탈퇴 회원 암호화 이메일", example = "dfaEDFE2")
    private String hashedEmail;

    @Schema(description = "탈퇴 회원 마스킹 이메일", example = "d***@sample.com")
    private String maskedEmail;

    @Schema(description = "탈퇴 회원 암호화 전화번호", example = "dfaEDFE2")
    private String hashedPhone;

    @Schema(description = "탈퇴 회원 마스킹 전화번호", example = "010-1234-****")
    private String maskedPhone;

    @Schema(description = "탈퇴 이유", example = "재가입 예정")
    private String reason;

    @Schema(description = "차단 여부", example = "false")
    private boolean isBlock;

    @Builder
    public WithdrawalMemberResponse(Long id, String hashedEmail, String maskedEmail, String hashedPhone, String maskedPhone, String reason, boolean isBlock) {
        this.id = id;
        this.hashedEmail = hashedEmail;
        this.maskedEmail = maskedEmail;
        this.hashedPhone = hashedPhone;
        this.maskedPhone = maskedPhone;
        this.reason = reason;
        this.isBlock = isBlock;
    }

    public static WithdrawalMemberResponse from(WithdrawalMember member) {
        return WithdrawalMemberResponse.builder()
                .id(member.getId())
                .hashedEmail(member.getHashedEmail())
                .maskedEmail(member.getMaskedEmail())
                .hashedPhone(member.getHashedPhone())
                .maskedPhone(member.getMaskedPhone())
                .reason(member.getReason())
                .isBlock(member.isBlock())
                .build();
    }

}
