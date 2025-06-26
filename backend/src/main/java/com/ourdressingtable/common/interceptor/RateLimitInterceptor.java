package com.ourdressingtable.common.interceptor;

import com.ourdressingtable.common.exception.ErrorCode;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Profile("!test")
@Component
@AllArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedissonClient redissonClient;

    private static final Bandwidth DEFAULT_LIMIT = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));

    private static final Map<String, Bandwidth>  URL_LIMIT = Map.of(
            "/api/auth/find-email", Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1))),
            "/api/auth/verification-code/email", Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(3))),
            "/api/auth/confirm-verification-code/email", Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(10))),
            "/api/reset-password/request", Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(5)))
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if(!URL_LIMIT.containsKey(path)) {
            return true;
        }
        String ip = getClientIpAddress(request);
        String userId = getAuthenticatedUserId();
        String key = "rate-limit:" + path + ":" + (userId != null ? "user:" + userId : "ip:" + ip);

        try {
            Bandwidth limit = URL_LIMIT.getOrDefault(key, DEFAULT_LIMIT);
            Redisson redisson = (Redisson) redissonClient;
            CommandAsyncExecutor executor = redisson.getCommandExecutor();
            RedissonBasedProxyManager proxyManager = RedissonBasedProxyManager.builderFor(executor).withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(5))).build();

            BucketConfiguration configuration = BucketConfiguration.builder().addLimit(limit).build();

            Bucket bucket = proxyManager.builder().build(key, () -> configuration);

            if(!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for IP: {}", ip);
                response.setStatus(ErrorCode.TOO_MANY_REQUESTS.getHttpStatus().value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        String.format("{\"message\":\"%s\", \"code\":\"%s\"}",
                                ErrorCode.TOO_MANY_REQUESTS.getMessage(),
                                ErrorCode.TOO_MANY_REQUESTS.getCode()));
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Rate limit error for IP: {}", ip, e);
            return true;
        }
    }

    private String getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return null;
    }

    /**
     * 실제 클라이언트 ip 주소
     * @param request
     * @return
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 첫 번째 IP만 사용(comma로 구분된 경우)
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}