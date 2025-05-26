package com.ourdressingtable.security.auth;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;
    private final long EXPIRE_TIME = 7 * 24 * 60 * 60L;

    public void saveTokenInfo(String email, String token, String ip, String ua) {
        Map<String, String> data = Map.of(
                "token", token,
                "ip", ip,
                "ua", ua
        );

        redisTemplate.opsForHash().putAll(email,data);
        redisTemplate.expire(email, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public boolean validate(String email, String token, String ip, String ua) {
        Map<Object, Object> data = redisTemplate.opsForHash().entries(email);
        return token.equals(data.get("token")) && ip.equals(data.get("ip")) && ua.equals(data.get("ua"));
    }

    public void deleteTokenInfo(String email) {
        redisTemplate.delete(email);
    }
}
