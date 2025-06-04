package com.ourdressingtable.member.service;

import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.WithdrawalMember;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.WithdrawalMemberRepository;
import com.ourdressingtable.member.service.impl.WithdrawalMemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("WithdrawalMemberService 테스트")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WithdrawalMemberServiceImplTest {

    @InjectMocks
    private WithdrawalMemberServiceImpl withdrawalMemberService;

    @Mock
    private WithdrawalMemberRepository withdrawalMemberRepository;

    @Nested
    @DisplayName("탈퇴 회원 테스트")
    class createWithdrawalMemberTest {

        @DisplayName("탈퇴 회원 저장 성공")
        @Test
        public void createWithdrawalMember_shouldReturnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);
            WithdrawalMemberRequest request = TestDataFactory.testWithdrawalMemberRequest();

            // when
            withdrawalMemberService.createWithdrawalMember(request, member);
            ArgumentCaptor<WithdrawalMember> captor = ArgumentCaptor.forClass(WithdrawalMember.class);
            verify(withdrawalMemberRepository).save(captor.capture());

            WithdrawalMember withdrawalMember = captor.getValue();
            assertEquals(member.getId(), withdrawalMember.getMember().getId());
            assertEquals(request.getReason(), withdrawalMember.getReason());
        }

        @DisplayName("탈퇴 회원 저장 실패 - 이메일 null")
        @Test
        void createWithdrawalMember_shouldFail_whenEmailIsNull() {
            // given
            Member member = TestDataFactory.testMemberWithEmailNull(1L);
            WithdrawalMemberRequest request = TestDataFactory.testWithdrawalMemberRequest();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> {
                withdrawalMemberService.createWithdrawalMember(request, member);
            });

            verify(withdrawalMemberRepository, never()).save(any());

        }
    }
}
