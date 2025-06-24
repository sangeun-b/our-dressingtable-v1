package com.ourdressingtable.member.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.common.util.SecurityUtilMockHelper;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.dto.request.CreateMemberRequest;
import com.ourdressingtable.member.dto.response.MemberResponse;
import com.ourdressingtable.member.dto.response.OtherMemberResponse;
import com.ourdressingtable.member.dto.request.UpdateMemberRequest;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.impl.MemberServiceImpl;

import java.util.Optional;

import com.ourdressingtable.auth.email.repository.EmailVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WithdrawalMemberService withdrawalMemberService;

    @Mock
    private EmailVerificationRepository  emailVerificationRepository;

    @Nested
    @DisplayName("회원 가입 테스트")
    class SingUpTest {
        @DisplayName("회원 가입 성공")
        @Test
        public void createMember_returnSuccess() {
            // given
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();
            String encodedPassword = "password";
            Member member = createMemberRequest.toEntity(encodedPassword);
            ReflectionTestUtils.setField(member,"id",1L);

            when(emailVerificationRepository.isVerified(createMemberRequest.getEmail())).thenReturn(true);
            when(passwordEncoder.encode(createMemberRequest.getPassword())).thenReturn(encodedPassword);
            when(memberRepository.existsByEmail(createMemberRequest.getEmail())).thenReturn(false);
            when(memberRepository.existsByNickname(createMemberRequest.getNickname())).thenReturn(false);
            when(memberRepository.save(any(Member.class))).thenReturn(member);

            // when
            Long savedId = memberService.createMember(createMemberRequest);

            //then
            assertNotNull(savedId);
            assertEquals(createMemberRequest.getEmail(), member.getEmail());

        }


        @DisplayName("회원 가입 실패 - 중복 이메일")
        @Test
        public void createMember_withDuplicateEmail_returnError() {
            // given
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();
            when(emailVerificationRepository.isVerified(createMemberRequest.getEmail())).thenReturn(true);
            when(memberRepository.existsByEmail(createMemberRequest.getEmail())).thenReturn(true);

            // when & then
            OurDressingTableException ourDressingTableException = assertThrows(OurDressingTableException.class, () -> memberService.createMember(createMemberRequest));
            assertEquals(ourDressingTableException.getHttpStatus(), ErrorCode.EMAIL_ALREADY_EXISTS.getHttpStatus());

        }

        @DisplayName("회원 가입 실패 - 이메일 미인증")
        @Test
        public void createMember_withUnverifiedEmail_returnError() {
            // given
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();
            when(emailVerificationRepository.isVerified(createMemberRequest.getEmail())).thenReturn(false);

            // when & then
            OurDressingTableException ourDressingTableException = assertThrows(OurDressingTableException.class, () -> memberService.createMember(createMemberRequest));
            assertEquals(ourDressingTableException.getHttpStatus(), ErrorCode.EMAIL_NOT_VERIFIED.getHttpStatus());

        }

        @DisplayName("회원 가입 실패 - 90일 이내 탈퇴 회원")
        @Test
        public void createMember_withRecentlyWithdrawnEmail_returnError() {
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();

            when(emailVerificationRepository.isVerified(createMemberRequest.getEmail())).thenReturn(true);
            when(memberRepository.existsByEmail(createMemberRequest.getEmail())).thenReturn(false);
            when(memberRepository.existsByNickname(createMemberRequest.getNickname())).thenReturn(false);

            doThrow(new OurDressingTableException(ErrorCode.WITHDRAWN_MEMBER_RESTRICTED, "재가입 가능일: 2025년 09월 18일"))
                    .when(withdrawalMemberService)
                    .validateWithdrawalMember(createMemberRequest.getEmail());

            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> memberService.createMember(createMemberRequest));

            assertEquals(exception.getHttpStatus(), ErrorCode.WITHDRAWN_MEMBER_RESTRICTED.getHttpStatus());
            assertTrue(exception.getMessage().contains("재가입 가능일"));
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class FindMemberTest {

        @DisplayName("회원 조회 성공 테스트")
        @Test
        public void findMember_returnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);

            given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

            // when
            OtherMemberResponse findMember = memberService.getOtherMember(member.getId());

            //then
            assertEquals(findMember.getNickname(),member.getNickname());

        }

        @DisplayName("회원 조회 실패 테스트 - USER NOT FOUND")
        @Test
        public void findMember_returnUserNotFoundError() {
            // given
            Member member = TestDataFactory.testMember(1L);
            given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

            // when
            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> memberService.getOtherMember(member.getId()));

            //then
            assertEquals(exception.getHttpStatus(), ErrorCode.MEMBER_NOT_FOUND.getHttpStatus());

        }
    }

    @Nested
    @DisplayName("회원 정보 수정 테스트")
    class UpdateMemberTest {

        @DisplayName("회원 정보 수정 성공 테스트")
        @Test
        public void updateMember_returnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);
            UpdateMemberRequest updateMemberRequest = TestDataFactory.testUpdateMemberRequest();

            try(MockedStatic<SecurityUtil> mockedStatic = Mockito.mockStatic(SecurityUtil.class)) {
                mockedStatic.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
                when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

                // when
                memberService.updateMember(updateMemberRequest);

                //then
                assertEquals(member.getNickname(), "new me");
            }

        }

        @DisplayName("회원 정보 수정 실패 테스트 - 탈퇴한 회원")
        @Test
        public void updateMember_returnUserNotFoundError() {
            // given
            Member member = TestDataFactory.testMember(1L);
            member.withdraw();
            UpdateMemberRequest updateMemberRequest = TestDataFactory.testUpdateMemberRequest();

            try(MockedStatic<SecurityUtil> mockedStatic = Mockito.mockStatic(SecurityUtil.class)) {
                mockedStatic.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
                when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

                // when & then
                OurDressingTableException exception = assertThrows(OurDressingTableException.class,
                        () -> memberService.updateMember(updateMemberRequest));
                assertEquals(ErrorCode.MEMBER_NOT_ACTIVE.getCode(), exception.getCode());
            }
        }
    }

    @Nested
    @DisplayName("회원 삭제 테스트")
    class DeleteMemberTest {

        @DisplayName("회원 삭제 성공")
        @Test
        public void deleteMember_returnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);
            WithdrawalMemberRequest withdrawalMemberRequest = TestDataFactory.testWithdrawalMemberRequest();

            try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
                mockedSecurityUtil.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
                when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
                when(passwordEncoder.matches(withdrawalMemberRequest.getPassword(), member.getPassword())).thenReturn(true);
                // when
                memberService.withdrawMember(withdrawalMemberRequest);

                // then
                assertEquals(Status.WITHDRAWAL, member.getStatus());
                verify(withdrawalMemberService).createWithdrawalMember(withdrawalMemberRequest, member);
            }

        }

        @DisplayName("회원 삭제 실패 - 이미 탈퇴한 회원")
        @Test
        public void updateMember_returnAlreadyWithdrawError() {
            // given
            Long memberId = 2L;
            Member member = TestDataFactory.testMember(2L);
            member.withdraw();

            WithdrawalMemberRequest withdrawalMemberRequest = TestDataFactory.testWithdrawalMemberRequest();
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            assertThrows(OurDressingTableException.class, () -> {
                memberService.withdrawMember(withdrawalMemberRequest);
            });

        }
    }

    @Nested
    @DisplayName("내 정보 조회 테스트")
    class GetMyInformationTest {

        @DisplayName("내 정보 조회 성공")
        @Test
        public void getMyInfo_returnSuccess() {
            Long memberId = 1L;
            Member member = TestDataFactory.testMember(1L);

            try (MockedStatic<SecurityUtil> mockedSecurityUtil = SecurityUtilMockHelper.mockCurrentMemberId(memberId)) {
                mockedSecurityUtil.when(SecurityUtil::getCurrentMemberId).thenReturn(memberId);
                when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

                MemberResponse response = memberService.getMyInfo();
                assertEquals(member.getNickname(), response.getNickname());
                assertEquals(member.getEmail(), response.getEmail());

            }



        }

        @DisplayName("내 정보 조회 실패 - 비활성 회원")
        @Test
        public void getMyInfo_returnMemberNotActiveError() {
            Long memberId = 2L;
            Member withDrawnMember = TestDataFactory.testMember(memberId);
            withDrawnMember.withdraw();

            try (MockedStatic<SecurityUtil> mockedSecurityUtil = SecurityUtilMockHelper.mockCurrentMemberId(memberId)) {
                mockedSecurityUtil.when(SecurityUtil::getCurrentMemberId).thenReturn(memberId);
                when(memberRepository.findById(memberId)).thenReturn(Optional.of(withDrawnMember));

                OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> {
                    memberService.getMyInfo();
                });

                assertEquals(ErrorCode.MEMBER_NOT_ACTIVE.getCode(), exception.getCode());
            }
        }
    }

    @Nested
    @DisplayName("이메일(ID) 찾기 테스트")
    class GetEmailTest {

        @DisplayName("이메일(ID) 찾기 성공")
        @Test
        public void getEmailByNameAndPhone_returnSuccess() {
            String name = "김이름";
            String phone = "010-1234-5678";
            String expectedEmail = "test@example.com";

            Member member = TestDataFactory.testMember(1L);

            given(memberRepository.findByNameAndPhoneNumber(name, phone)).willReturn(Optional.of(member));

            String result = memberService.getEmailByNameAndPhone(name, phone);
            assertEquals(expectedEmail, result);

        }

        @DisplayName("이메일(ID) 찾기 실패 - 존재하지 않는 회원")
        @Test
        public void getEmailByNameAndPhone_returnMemberNotFoundError() {
            String name = "비회원";
            String phone = "010-5555-5678";

            given(memberRepository.findByNameAndPhoneNumber(name, phone)).willReturn(Optional.empty());

            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () ->
                    memberService.getEmailByNameAndPhone(name, phone));

            assertEquals(ErrorCode.MEMBER_NOT_FOUND.getCode(), exception.getCode());

        }

    }
}
