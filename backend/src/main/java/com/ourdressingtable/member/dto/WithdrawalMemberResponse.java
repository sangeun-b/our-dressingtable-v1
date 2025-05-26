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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalMemberResponse {

    @Schema(description = "생성된 탈퇴 회원 ID", example = "1")
    private Long id;
}
