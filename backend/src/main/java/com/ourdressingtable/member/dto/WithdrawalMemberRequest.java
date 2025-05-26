package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.WithdrawalMember;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class WithdrawalMemberRequest {

    @Schema(description = "탈퇴 회원 이유", example = "재가입 예정")
    @NotBlank
    @Size(max=500)
    private String reason;

    @Schema(description = "차단 여부", example = "FALSE")
    private boolean isBlock;

    @Builder
    public WithdrawalMemberRequest(String reason, boolean isBlock) {
        this.reason = reason;
        this.isBlock = isBlock;
    }

}
