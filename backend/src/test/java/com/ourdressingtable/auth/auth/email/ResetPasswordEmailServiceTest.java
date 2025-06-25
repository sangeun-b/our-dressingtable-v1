package com.ourdressingtable.auth.auth.email;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.auth.email.repository.ResetTokenRepository;
import com.ourdressingtable.auth.email.service.EmailService;
import com.ourdressingtable.auth.email.service.ResetPasswordEmailServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("비밀번호 재설정 기능 테스트")
public class ResetPasswordEmailServiceTest {

    @InjectMocks
    private ResetPasswordEmailServiceImpl resetPasswordEmailService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private ResetTokenRepository resetTokenRepository;

    @Nested
    @DisplayName("비밀번호 재설정 링크 전송 테스트")
    class SendResetLinkTest {

        @DisplayName("비밃먼호 재설정 링크 전송 성공")
        @Test
        public void sendResetLink_returnSuccess() {
            String email = "test@example.com";
            given(memberRepository.existsByEmail(email)).willReturn(true);

            assertDoesNotThrow(() -> resetPasswordEmailService.sendResetLink(email));
            verify(resetTokenRepository).save(anyString(), eq(email), any());
            verify(emailService).send(eq(email), contains("비밀번호 재설정 링크"), contains("아래 링크를"));
        }

        @DisplayName("비밃먼호 재설정 링크 전송 실패 - 존재하지 않는 이메일")
        @Test
        public void sendResetLink_returnNotFoundError() {
            String email = "notfound@example.com";
            given(memberRepository.existsByEmail(email)).willReturn(false);

           assertThrows(OurDressingTableException.class, () ->
                   resetPasswordEmailService.sendResetLink(email));
        }
    }

    @Nested
    @DisplayName("비밀번호 재설정 테스트")
    class ResetPasswordConfirm {

        @DisplayName("비밀번호 재설정 성공")
        @Test
        public void resetPasswordConfirm_returnSuccess() {
            String token = "valid-token";
            String email = "test@example.com";
            String newPassword = "newPassword123!";
            String encodedPassword = "encodedPassword";

            Member member = TestDataFactory.testMember(1L);

            given(resetTokenRepository.getEmailByToken(token)).willReturn(Optional.of(email));
            given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
            given(passwordEncoder.encode(newPassword)).willReturn(encodedPassword);

            resetPasswordEmailService.resetPassword(token, newPassword);

            assertEquals(encodedPassword, member.getPassword());
            verify(resetTokenRepository).delete(token);
        }

        @DisplayName("비밀번호 재설정 실패 - 토큰 없음")
        @Test
        public void resetPasswordConfirm_returnTokenNotFoundError() {
            String token = "expired-token";
            String newPassword = "pw";

            given(resetTokenRepository.getEmailByToken(token)).willReturn(Optional.empty());

            assertThrows(OurDressingTableException.class, () ->
                    resetPasswordEmailService.resetPassword(token, newPassword));

        }

        @DisplayName("비밀번호 재설정 실패 - 토큰 없음")
        @Test
        public void resetPasswordConfirm_returnMemberNotFoundError() {
            String token = "valid-token";
            String email = "no-user@example.com";
            String newPassword = "pw";

            given(resetTokenRepository.getEmailByToken(token)).willReturn(Optional.of(email));
            given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

            assertThrows(OurDressingTableException.class, () ->
                    resetPasswordEmailService.resetPassword(token, newPassword));

        }
    }

}
