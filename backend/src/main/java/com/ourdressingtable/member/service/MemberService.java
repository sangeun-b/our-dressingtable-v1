package com.ourdressingtable.member.service;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.dto.request.CreateMemberRequest;
import com.ourdressingtable.member.dto.response.MemberResponse;
import com.ourdressingtable.member.dto.response.OtherMemberResponse;
import com.ourdressingtable.member.dto.request.UpdateMemberRequest;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);
    OtherMemberResponse getOtherMember(Long id);
    void updateMember(UpdateMemberRequest updateMemberRequest);
    void withdrawMember(WithdrawalMemberRequest withdrawalMemberRequest );
    Member getMemberEntityById(Long id);
    Member getActiveMemberEntityById(Long id);
    Member getActiveMemberEntityByEmail(String email);
    MemberResponse getMyInfo();
    void verifyResettableEmail(@NotBlank(message = "이메일을 입려해주세요.") @Email(message = "올바른 이메일 형식이 아닙니다.") String email);
    void resetPassword(@NotBlank(message = "이메일을 입려해주세요.") @Email(message = "올바른 이메일 형식이 아닙니다.") String email, @NotBlank(message = "새로운 비밀번호를 입려해주세요.") String newPassword);
}
