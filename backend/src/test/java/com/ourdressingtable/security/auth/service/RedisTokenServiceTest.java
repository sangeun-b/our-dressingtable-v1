package com.ourdressingtable.security.auth.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.security.auth.RedisTokenService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RedisTokenServiceTest {

    @InjectMocks
    private RedisTokenService redisTokenService;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    public void setUp() {
        when(stringRedisTemplate.opsForHash()).thenReturn(hashOperations);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Nested
    @DisplayName("블랙리스트 테스트")
    class BlacklistAccessToken {

        @DisplayName("블랙리스트 등록 성공")
        @Test
        public void blacklistAccessToken_returnSuccess() {
            // when
            redisTokenService.blacklistAccessToken("access-token", 1000L);

            // then
            verify(valueOperations).set(eq("blacklist:access-token"), eq("logout"), eq(1000L), eq(
                    TimeUnit.MILLISECONDS));

        }
    }

    @Nested
    @DisplayName("토큰 테스트")
    class Token {
        @DisplayName("토큰 검증 성공")
        @Test
        public void token_returnSuccess() {
            // given
            String rawToken = "correct-token";
            String hashedToken = hashToken(rawToken);
            Map<Object, Object> data = Map.of("token", hashedToken, "ip", "127.0.0.1", "ua", "User-Agent");
            when(hashOperations.entries(anyString())).thenReturn(data);

            // when
            boolean result = redisTokenService.validate("test@exampl.com", rawToken, "127.0.0.1", "User-Agent", "refreshToken");

            // then
            assertTrue(result);
        }
        @Test
        @DisplayName("토큰 검증 실패 - 데이터 없음")
        void validate_noData_shouldThrowUnauthorized() {
            // given
            when(hashOperations.entries(anyString())).thenReturn(Map.of());

            // then
            assertThrows(OurDressingTableException.class, () -> {
                redisTokenService.validate("test@example.com", "token", "127.0.0.1", "User-Agent", "refreshToken");
            });
        }
        @Test
        @DisplayName("토큰 검증 실패 - 토큰 불일치")
        void validate_tokenMismatch_shouldThrowUnauthorized() {
            // given
            String hashedWrongToken = hashToken("wrong-token");
            Map<Object, Object> data = Map.of("token", hashedWrongToken, "ip", "127.0.0.1", "ua", "User-Agent");
            when(hashOperations.entries(anyString())).thenReturn(data);

            // then
            assertThrows(OurDressingTableException.class, () -> {
                redisTokenService.validate("test@example.com", "correct-token", "127.0.0.1", "User-Agent", "refreshToken");
            });
        }
    }

    private String hashToken(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = md.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }



}
