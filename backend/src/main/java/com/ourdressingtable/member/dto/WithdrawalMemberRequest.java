package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.WithdrawalMember;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String reason;
    private boolean isBlock;

    @Builder
    public WithdrawalMemberRequest(String reason, String email, String phone, boolean isBlock) {
        this.reason = reason;
    }

    public WithdrawalMember toEntity(String maskedEmail, String hashedEmail, String maskedPhone, String hashedPhone, boolean isBlock) {
        return WithdrawalMember.builder()
                .reason(reason)
                .hashedEmail(hashedEmail)
                .maskedEmail(maskedEmail)
                .maskedPhone(maskedPhone)
                .hashedPhone(hashedPhone)
                .isBlock(isBlock)
                .build();
    }
}
