package com.ourdressingtable.common.interceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Slf4j
@Profile("!test")
@Component
@AllArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedissonClient redissonClient;

    private final Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(5)));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!request.getRequestURI().equals("/api/auth/find-email")){
            return true;
        }

        String ip = getClientIpAddress(request);
        String key = "rate-limit:find-email:" + ip;

        try {
            CommandAsyncExecutor commandExecutor = (CommandAsyncExecutor) redissonClient;
            RedissonBasedProxyManager proxyManager = RedissonBasedProxyManager.builderFor(commandExecutor).build();

            BucketConfiguration configuration = BucketConfiguration.builder().addLimit(limit).build();

            Bucket bucket = proxyManager.builder().build(key, () -> configuration);

            if(!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for IP: {}", ip);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"요청이 너무 많습니다. 잠시 후 다시 시도해주세요.\"}");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Rate limit error for IP: {}", ip, e);
            return true;
        }
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
