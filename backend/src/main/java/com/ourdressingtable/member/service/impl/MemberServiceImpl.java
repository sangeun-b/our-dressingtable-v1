package com.ourdressingtable.member.service.impl;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.dto.request.CreateMemberRequest;
import com.ourdressingtable.member.dto.response.MemberResponse;
import com.ourdressingtable.member.dto.response.OtherMemberResponse;
import com.ourdressingtable.member.dto.request.UpdateMemberRequest;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.member.service.WithdrawalMemberService;
import com.ourdressingtable.security.auth.email.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final WithdrawalMemberService withdrawalMemberService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    @Transactional
    public Long createMember(CreateMemberRequest createMemberRequest) {

        if (!emailVerificationRepository.isVerified(createMemberRequest.getEmail())) {
            throw new OurDressingTableException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        if (memberRepository.existsByEmail(createMemberRequest.getEmail())) {
            throw new OurDressingTableException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (memberRepository.existsByNickname(createMemberRequest.getNickname())) {
            throw new OurDressingTableException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        withdrawalMemberService.validateWithdrawalMember(createMemberRequest.getEmail());

        String encodedPassword = passwordEncoder.encode(createMemberRequest.getPassword());
        Member member = createMemberRequest.toEntity(encodedPassword);
        member.active();
        return memberRepository.save(member).getId();
    }

    @Override
    public OtherMemberResponse getOtherMember(Long id) {
        Member member = getMemberByIdAndStatus(id, Status.ACTIVE, ErrorCode.MEMBER_NOT_ACTIVE);
        return OtherMemberResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public void updateMember(UpdateMemberRequest updateMemberRequest) {
        Member member = getMemberByIdAndStatus(SecurityUtil.getCurrentMemberId(), Status.ACTIVE, ErrorCode.MEMBER_NOT_ACTIVE);
        member.updateMember(updateMemberRequest);
    }

    @Override
    @Transactional
    public void withdrawMember(WithdrawalMemberRequest withdrawalMemberRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = getMemberEntityById(memberId);

        if (member.getStatus() == Status.BLOCK || member.getStatus() == Status.WITHDRAWAL) {
            throw new OurDressingTableException(ErrorCode.ALREADY_WITHDRAW_OR_BLOCKED);
        }

        if (!passwordEncoder.matches(withdrawalMemberRequest.getPassword(), member.getPassword())) {

            throw new OurDressingTableException(ErrorCode.INVALID_PASSWORD);
        }
        member.withdraw();
        withdrawalMemberService.createWithdrawalMember(withdrawalMemberRequest, member);
    }

    @Override
    public Member getMemberEntityById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member getActiveMemberEntityById(Long id) {
        return getMemberByIdAndStatus(id, Status.ACTIVE, ErrorCode.MEMBER_NOT_ACTIVE);
    }

    @Override
    public Member getActiveMemberEntityByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
        validateStatus(member, Status.ACTIVE, ErrorCode.MEMBER_NOT_ACTIVE);
        return member;

    }

    @Override
    public MemberResponse getMyInfo() {
        Member member = getActiveMemberEntityById(SecurityUtil.getCurrentMemberId());
        return MemberResponse.from(member);
    }

    private Member getMemberByIdAndStatus(Long id, Status status, ErrorCode errorCode) {
        Member member = getMemberEntityById(id);
        validateStatus(member, status, errorCode);
        return member;
    }

    private void validateStatus(Member member, Status requiredStatus, ErrorCode errorCode) {
        if(member.getStatus() != requiredStatus) {
            throw new OurDressingTableException(errorCode);
        }
    }

}
