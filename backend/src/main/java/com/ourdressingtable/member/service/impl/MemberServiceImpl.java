package com.ourdressingtable.member.service.impl;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.domain.WithdrawalMember;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.repository.WithdrawalMemberRepository;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final WithdrawalMemberRepository withdrawalMemberRepository;

    @Override
    @Transactional
    public Long createMember(CreateMemberRequest createMemberRequest) {
        boolean isExists = memberRepository.existsByEmail(createMemberRequest.getEmail());

        if (isExists) {
            throw new OurDressingTableException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = memberRepository.save(createMemberRequest.toEntity());
        return member.getId();
    }

    @Override
    public OtherMemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
        return OtherMemberResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public void updateMember(Long id, UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(()->new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateMember(updateMemberRequest);
    }

    @Override
    @Transactional
    public void deleteMember(Long id, WithdrawalMemberRequest withdrawalMemberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
        Long newId = createWithdrawalMember(withdrawalMemberRequest, member);
        if(newId != null) {
            memberRepository.delete(member);
        }
    }

    @Override
    @Transactional
    public Long createWithdrawalMember(WithdrawalMemberRequest withdrawalMemberRequest, Member member) {
        boolean isBlock = member.getStatus() == Status.BLOCK;

        WithdrawalMember withdrawalMember = WithdrawalMember.from(member, withdrawalMemberRequest, isBlock);
        withdrawalMemberRepository.save(withdrawalMember);
        return withdrawalMember.getId();
    }

    @Override
    public Member getMemberEntityById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
    }



}
