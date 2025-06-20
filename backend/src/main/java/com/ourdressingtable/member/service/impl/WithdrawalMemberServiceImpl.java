package com.ourdressingtable.member.service.impl;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.domain.WithdrawalMember;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.WithdrawalMemberRepository;
import com.ourdressingtable.member.service.WithdrawalMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawalMemberServiceImpl implements WithdrawalMemberService {

    private final WithdrawalMemberRepository withdrawalMemberRepository;

    @Override
    public void createWithdrawalMember(WithdrawalMemberRequest request, Member member) {

        boolean isBlocked = member.getStatus() == Status.BLOCK;

        WithdrawalMember withdrawalMember = WithdrawalMember.from(member, request, isBlocked);
        withdrawalMemberRepository.save(withdrawalMember);
    }

}
