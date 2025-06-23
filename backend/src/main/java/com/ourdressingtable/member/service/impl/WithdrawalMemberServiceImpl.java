package com.ourdressingtable.member.service.impl;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.HashUtil;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.domain.WithdrawalMember;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.WithdrawalMemberRepository;
import com.ourdressingtable.member.service.WithdrawalMemberService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javax.swing.text.html.Option;
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

    @Override
    public void validateWithdrawalMember(String email) {
        String hashedEmail = HashUtil.hash(email);
        Optional<WithdrawalMember> withdrawalMember = withdrawalMemberRepository.findByHashedEmail(hashedEmail);

        if (withdrawalMember.isPresent()) {
            WithdrawalMember member = withdrawalMember.get();

            if(member.isBlock()) {
                throw new OurDressingTableException(ErrorCode.WITHDRAWN_BLOCK_MEMBER_RESTRICTED);
            }

            LocalDateTime withdrewAt = member.getWithdrewAt().toLocalDateTime();
            LocalDateTime availableAt = withdrewAt.plusDays(90);
            if(withdrewAt.plusDays(90).isAfter(LocalDateTime.now())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                String formattedDate = availableAt.format(formatter);
                String msg = String.format("탈퇴 후 90일 이내에 재가입이 불가능합니다. 재가입 가능일: %s", formattedDate);
                throw new OurDressingTableException(ErrorCode.WITHDRAWN_MEMBER_RESTRICTED, msg);
            }
        }
    }
}
