package com.ourdressingtable.security.auth.email;


import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.security.auth.email.repository.EmailVerificationRepository;
import com.ourdressingtable.security.auth.email.service.EmailService;
import com.ourdressingtable.security.auth.email.service.EmailVerificationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static com.ourdressingtable.member.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("이메일 인증코드 기능 테스트")
public class EmailVerificationServiceImplTest {

    @InjectMocks
    private EmailVerificationServiceImpl emailVerificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Nested
    @DisplayName("이메일 인증코드 전송 테스트")
    class SendVerificationEmail {

        @DisplayName("이메일 인증코드 전송 성공")
        @Test
        void sendVerificationEmail_returnSuccess() {
            String email = "sample@example.com";

            emailVerificationService.sendVerificationEmail(email);

            then(emailVerificationRepository).should().save(eq(email), anyString());
            then(emailService).should().send(eq(email), contains("이메일 인증코드"), contains("인증코드는"));

        }

    }

    @Nested
    @DisplayName("이메일 인증코드 확인 테스트")
    class confirmVerificationEmail {

        @DisplayName("이메일 인증코드 확인 성공")
        @Test
        void confirmVerificationEmail_returnSuccess() {
            String email = "sample@example.com";
            String code = "123456";

            given(emailVerificationRepository.getVerificationCode(email)).willReturn(Optional.of(code));

            emailVerificationService.confirmVerification(email, code);
            then(emailVerificationRepository).should().markVerified(email);

        }

        @DisplayName("이메일 인증코드 확인 실패 - 불일치")
        @Test
        void confirmVerificationEmail_returnInvalidCodeError() {
            String email = "sample@example.com";
            String code = "123456";
            String wrongCode = "000000";

            given(emailVerificationRepository.getVerificationCode(email)).willReturn(Optional.of(code));

            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> {
                emailVerificationService.confirmVerification(email, wrongCode);
            });

            assertThat(exception.getCode()).isEqualTo(ErrorCode.INVALID_VERIFICATION_CDOE.getCode());

        }

        @DisplayName("이메일 인증코드 확인 실패 - 만료된 인증코드")
        @Test
        void confirmVerificationEmail_returnExpiredCodeError() {
            String email = "sample@example.com";

            given(emailVerificationRepository.getVerificationCode(email)).willReturn(Optional.empty());

            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> {
                emailVerificationService.confirmVerification(email, "123456");
            });

            assertThat(exception.getCode()).isEqualTo(ErrorCode.EXPIRED_VERIFICATION_CDOE.getCode());
        }

    }


}
