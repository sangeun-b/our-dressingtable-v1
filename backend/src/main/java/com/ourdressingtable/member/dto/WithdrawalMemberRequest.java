package com.ourdressingtable.member.dto;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.WithdrawalMember;
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

    @NotBlank
    @Size(max=500)
    private String reason;

    private boolean isBlock;

    @Builder
    public WithdrawalMemberRequest(String reason, boolean isBlock) {
        this.reason = reason;
        this.isBlock = isBlock;
    }

}
