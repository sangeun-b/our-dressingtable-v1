package com.ourdressingtable.auth.email.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String CODE_PREFIX = "email::verify::code";
    private static final String VERIFIED_PREFIX = "email::verify::verified";
    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final Duration VERIFIED_TTL = Duration.ofMinutes(10);

    @Override
    public void save(String email, String verificationCode) {
        stringRedisTemplate.opsForValue().set(CODE_PREFIX + email, verificationCode, CODE_TTL);
    }

    @Override
    public Optional<String> getVerificationCode(String email) {
        String code = stringRedisTemplate.opsForValue().get(CODE_PREFIX + email);
        return Optional.ofNullable(code);
    }

    @Override
    public void markVerified(String email) {
        stringRedisTemplate.opsForValue().set(VERIFIED_PREFIX + email, "true", VERIFIED_TTL);
    }

    @Override
    public boolean isVerified(String email) {
        return "true".equals(stringRedisTemplate.opsForValue().get(VERIFIED_PREFIX + email));
    }
}
