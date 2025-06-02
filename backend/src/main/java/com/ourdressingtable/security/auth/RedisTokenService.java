package com.ourdressingtable.security.auth;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;
    private final long EXPIRE_TIME = 7 * 24 * 60 * 60L; // 7일

    private String generateKey(String email, String type, String deviceId) {
        return email + ":" + type + ":" + deviceId;
    }

    private String getDeviceId(String ua) {
        if(ua == null)
            return "default-device";
        return Integer.toHexString(ua.hashCode());
    }

    public void saveTokenInfo(String email, String token, String ip, String ua, String type) {
        String deviceId = getDeviceId(ua);
        String redisKey = generateKey(email, type, deviceId);
        Map<String, String> data = Map.of(
                "token", token,
                "ip", ip,
                "ua", ua
        );
        redisTemplate.opsForHash().putAll(redisKey,data);
        redisTemplate.expire(redisKey, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public boolean validate(String email, String token, String ip, String ua, String type) {
        String deviceId = getDeviceId(ua);
        String redisKey = generateKey(email, type, deviceId);
        Map<Object, Object> data = redisTemplate.opsForHash().entries(redisKey);
        if(data.isEmpty()) {
            throw new OurDressingTableException(ErrorCode.UNAUTHORIZED);
        }
        if(!token.equals(data.get("token"))) {
            throw new OurDressingTableException(ErrorCode.UNAUTHORIZED);
        }
        if(!ip.equals(data.get("ip")) || !ua.equals(data.get("ua"))) {
            throw new OurDressingTableException(ErrorCode.FORBIDDEN);
        }
        return token.equals(data.get("token"));
    }

    public void deleteTokenInfo(String email, String type, String ua) {
        String deviceId = getDeviceId(ua);
        String redisKey = generateKey(email, type, deviceId);
        log.info("Redis Token Delete Start:", redisKey);
        Boolean result = redisTemplate.delete(redisKey);
        if(!result) {
            log.warn("Redis Token Delete Fail: email={}, type={}, deviceId={}", email, type, deviceId);
            throw new OurDressingTableException(ErrorCode.INTERNAL_SEVER_ERROR);
        }
        log.info("Redis Token Delete End:", redisKey);
    }

    public void blacklistAccessToken(String accessToken, long expireTime) {
        redisTemplate.opsForValue().set("blacklist:"+accessToken, "logout", expireTime, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String accessToken) {
        boolean blacklisted = redisTemplate.hasKey("blacklist:" + accessToken);
        if(blacklisted) {
            throw new OurDressingTableException(ErrorCode.UNAUTHORIZED);
        }
        return false;
    }
}
