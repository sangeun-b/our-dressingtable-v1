package com.ourdressingtable.auth.email.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.auth.email.repository.ResetTokenRepository;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordEmailServiceImpl implements ResetPasswordEmailService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ResetTokenRepository resetTokenRepository;

    private static final Duration TOKEN_TTL = Duration.ofMinutes(15);

    @Override
    public void sendResetLink(String email) {
        if(!memberRepository.existsByEmail(email)){
            throw new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String token = UUID.randomUUID().toString();
        resetTokenRepository.save(token, email, TOKEN_TTL);

        String link = "domain url" + token;
        String subject = "[우리의 화장대] 비밀번호 재설정 링크";
        String body = String.format("아래 링크를 통해 비밀번호를 재설정하세요. 링크는 15분 동안 유효합니다. \n\n%s", link);
        emailService.send(email, subject, body);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        String email = resetTokenRepository.getEmailByToken(token)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.EXPIRED_VERIFICATION_CDOE));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedNewPassword);

        resetTokenRepository.delete(token);

    }

}
