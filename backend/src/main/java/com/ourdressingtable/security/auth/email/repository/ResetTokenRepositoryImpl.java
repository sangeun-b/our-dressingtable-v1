package com.ourdressingtable.security.auth.email.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ResetTokenRepositoryImpl implements ResetTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "reset::token::";

    @Override
    public void save(String token, String email, Duration ttl) {
        redisTemplate.opsForValue().set(PREFIX + token, email,ttl);
    }

    @Override
    public Optional<String> getEmailByToken(String token) {
        String email = redisTemplate.opsForValue().get(PREFIX + token);
        return Optional.ofNullable(email);
    }

    @Override
    public void delete(String token) {
        redisTemplate.delete(PREFIX + token);
    }

}
