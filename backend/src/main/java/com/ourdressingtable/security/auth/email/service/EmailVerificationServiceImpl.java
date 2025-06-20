package com.ourdressingtable.security.auth.email.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.security.auth.email.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService{

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    @Override
    public void sendVerificationEmail(String email) {
        String code = generateVerificationCode();
        emailVerificationRepository.save(email, code);
        String subject = "[우리의 화장대] 이메일 인증코드";
        String body = String.format("인증코드는 [%s] 입니다. 5분 내에 입력해주세요.", code);
        emailService.send(email, subject, body);

    }

    @Override
    public void confirmVerification(String email, String code) {
        Optional<String> saveCode = emailVerificationRepository.getVerificationCode(email);
        if (saveCode.isEmpty()) {
            throw new OurDressingTableException(ErrorCode.EXPIRED_VERIFICATION_CDOE);
        }

        if (!saveCode.get().equals(code)) {
            throw new OurDressingTableException(ErrorCode.INVALID_VERIFICATION_CDOE);
        }

        emailVerificationRepository.markVerified(email);
    }

    private String generateVerificationCode() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000));
    }
}
