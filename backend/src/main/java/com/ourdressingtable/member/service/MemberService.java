package com.ourdressingtable.member.service;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);
    OtherMemberResponse getOtherMember(Long id);
    void updateMember(Long id, UpdateMemberRequest updateMemberRequest);
    void withdrawMember(Long id, WithdrawalMemberRequest withdrawalMemberRequest );
    Member getMemberEntityById(Long id);
    Member getActiveMemberEntityById(Long id);
    Member getActiveMemberEntityByEmail(String email);
}
