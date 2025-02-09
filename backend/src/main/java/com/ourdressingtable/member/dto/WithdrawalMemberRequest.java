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

    @Builder
    public WithdrawalMemberRequest(String reason) {
        this.reason = reason;
    }

    public WithdrawalMember toEntity() {
        return WithdrawalMember.builder()
                .reason(reason)
                .build();
    }
}
