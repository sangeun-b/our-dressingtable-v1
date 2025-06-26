package com.ourdressingtable.auth.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LoginRateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_FAIL_COUNT = 5;
    private static final Duration BLOCK_DURATION = Duration.ofSeconds(10);

    public void checkLimit(String email, String ip) {
        if (isBlocked("login-fail:ip:"+ip) || isBlocked("login-fail:email:"+email)) {
            throw new OurDressingTableException(ErrorCode.TOO_MANY_LOGIN_REQUESTS);
        }
    }

    public void onLoginFail(String email, String ip) {
        increment("login-fail:ip:"+ip);
        increment("login-fail:email:"+email);
    }

    public void onLoginSuccess(String email, String ip) {
        redisTemplate.delete("login-fail:ip:"+ip);
        redisTemplate.delete("login-fail:email:"+email);
    }

    private boolean isBlocked(String key) {
        String countStr = redisTemplate.opsForValue().get(key);
        if (countStr == null) return false;
        int count = Integer.parseInt(countStr);
        return count >= MAX_FAIL_COUNT;
    }

    private void increment(String key) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, BLOCK_DURATION);
        }
    }
}
