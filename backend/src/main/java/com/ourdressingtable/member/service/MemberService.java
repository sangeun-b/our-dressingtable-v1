package com.ourdressingtable.member.service;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.MemberResponse;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);
    OtherMemberResponse getMember(Long id);
    void updateMember(Long id, UpdateMemberRequest updateMemberRequest);
    void deleteMember(Long id, WithdrawalMemberRequest withdrawalMemberRequest );
    Long createWithdrawalMember(WithdrawalMemberRequest withdrawalMemberRequest, Member member);
}
