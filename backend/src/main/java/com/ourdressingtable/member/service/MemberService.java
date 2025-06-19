package com.ourdressingtable.member.service;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.dto.request.CreateMemberRequest;
import com.ourdressingtable.member.dto.response.MemberResponse;
import com.ourdressingtable.member.dto.response.OtherMemberResponse;
import com.ourdressingtable.member.dto.request.UpdateMemberRequest;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);
    OtherMemberResponse getOtherMember(Long id);
    void updateMember(UpdateMemberRequest updateMemberRequest);
    void withdrawMember(WithdrawalMemberRequest withdrawalMemberRequest );
    Member getMemberEntityById(Long id);
    Member getActiveMemberEntityById(Long id);
    Member getActiveMemberEntityByEmail(String email);
    MemberResponse getMyInfo();
}
