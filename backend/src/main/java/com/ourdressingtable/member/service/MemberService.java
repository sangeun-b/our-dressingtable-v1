package com.ourdressingtable.member.service;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.security.dto.CustomUserDetails;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);
    OtherMemberResponse getOtherMember(Long id);
    void updateMember(UpdateMemberRequest updateMemberRequest);
    void withdrawMember(WithdrawalMemberRequest withdrawalMemberRequest );
    Member getMemberEntityById(Long id);
    Member getActiveMemberEntityById(Long id);
    Member getActiveMemberEntityByEmail(String email);
}
