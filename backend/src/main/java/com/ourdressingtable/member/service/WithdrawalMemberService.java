package com.ourdressingtable.member.service;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;

public interface WithdrawalMemberService {

    void createWithdrawalMember(WithdrawalMemberRequest withdrawalMemberRequest, Member member);
    void validateWithdrawalMember(String email);
}
