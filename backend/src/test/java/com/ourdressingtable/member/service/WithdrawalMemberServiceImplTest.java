package com.ourdressingtable.member.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.HashUtil;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.WithdrawalMember;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.WithdrawalMemberRepository;
import com.ourdressingtable.member.service.impl.WithdrawalMemberServiceImpl;
import java.time.LocalDateTime;
import java.util.Optional;
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
import static org.mockito.Mockito.when;

@DisplayName("탈퇴 회원 테스트")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WithdrawalMemberServiceImplTest {

    @InjectMocks
    private WithdrawalMemberServiceImpl withdrawalMemberService;

    @Mock
    private WithdrawalMemberRepository withdrawalMemberRepository;

    @Nested
    @DisplayName("탈퇴 회원 저장 테스트")
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

    @Nested
    @DisplayName("탈퇴 회원 통과 테스트")
    class validateWithDrawlMember {

        @DisplayName("탈퇴 회원 통과 성공 - 회원 이력 없는 회원")
        @Test
        void validateWithdrawalMember_returnSuccess() {
            String email = "sample@example.com";
            String hashedEmail = HashUtil.hash(email);
            when(withdrawalMemberRepository.findByHashedEmail(hashedEmail))
                    .thenReturn(Optional.empty());

            assertDoesNotThrow(() -> withdrawalMemberService.validateWithdrawalMember(email));
        }

        @DisplayName("탈퇴 회원 통과 실패 - BLOCK된 탈퇴 회원")
        @Test
        void validateWithdrawalMember_withBlock_returnError() {
            String email = "sample@example.com";
            String hashedEmail = HashUtil.hash(email);
            WithdrawalMember withdrawalMember = TestDataFactory.testWithdrawalMember(email, LocalDateTime.now().plusDays(100),true);

            when(withdrawalMemberRepository.findByHashedEmail(hashedEmail))
                    .thenReturn(Optional.of(withdrawalMember));

            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () ->
                    withdrawalMemberService.validateWithdrawalMember(email));

            assertEquals(ErrorCode.WITHDRAWN_BLOCK_MEMBER_RESTRICTED.getCode(), exception.getCode());

        }

        @DisplayName("탈퇴 회원 통과 성공 - 90일 초과 탈퇴자")
        @Test
        void validateWithdrawalMember_after90Days_returnSuccess() {
            String email = "sample@example.com";
            String hashedEmail = HashUtil.hash(email);
            WithdrawalMember withdrawalMember = TestDataFactory.testWithdrawalMember(email,
                    LocalDateTime.now().minusDays(111), false);
            when(withdrawalMemberRepository.findByHashedEmail(hashedEmail))
                    .thenReturn(Optional.of(withdrawalMember));

            assertDoesNotThrow(() -> withdrawalMemberService.validateWithdrawalMember(email));
        }

        @DisplayName("탈퇴 회원 통과 실패 - 90일 이내 탈퇴자")
        @Test
        void validateWithdrawalMember_within90Days_returnError() {

            String email = "sample@example.com";
            String hashedEmail = HashUtil.hash(email);
            WithdrawalMember withdrawalMember = TestDataFactory.testWithdrawalMember(email,
                    LocalDateTime.now().minusDays(20), false);
            when(withdrawalMemberRepository.findByHashedEmail(hashedEmail))
                    .thenReturn(Optional.of(withdrawalMember));
        
            OurDressingTableException exception = assertThrows(OurDressingTableException.class,
                    () -> withdrawalMemberService.validateWithdrawalMember(email));
            assertEquals(ErrorCode.WITHDRAWN_MEMBER_RESTRICTED.getCode(), exception.getCode());
            assertTrue(exception.getMessage().contains("재가입 가능일"));
        }
        
    }
}


