package com.ourdressingtable.member.service.impl;

import com.ourdressingtable.exception.ErrorCode;
import com.ourdressingtable.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.ColorType;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.SkinType;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.domain.WithdrawalMember;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.repository.WithdrawalMemberRepository;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.util.HashUtil;
import com.ourdressingtable.util.MaskingUtil;
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
        String maskedEmail = MaskingUtil.maskedEmail(member.getEmail());
        String maskedPhone = MaskingUtil.maskedPhone(member.getPhoneNumber());
        String hashedEmail = HashUtil.hash(member.getEmail());
        String hashedPhone = HashUtil.hash(member.getPhoneNumber());
        boolean isBlock = false;
        if(member.getStatus() == Status.BLOCK) {
            isBlock = true;
        }
        WithdrawalMember withdrawalMember = withdrawalMemberRepository.save(withdrawalMemberRequest.toEntity(hashedEmail,hashedPhone,maskedEmail,maskedPhone, isBlock));
        return withdrawalMember.getId();
    }



}
