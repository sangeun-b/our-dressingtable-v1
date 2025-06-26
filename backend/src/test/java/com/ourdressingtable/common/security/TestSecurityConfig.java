package com.ourdressingtable.common.security;

import com.ourdressingtable.common.interceptor.RateLimitInterceptor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.when;

@TestConfiguration(proxyBeanMethods = false)
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/dressing-tables/mine").authenticated()
                        .anyRequest().permitAll())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        RateLimitInterceptor interceptor = mock(RateLimitInterceptor.class);
        try {
            doReturn(true).when(interceptor).preHandle(any(), any(), any());
        } catch (Exception e) {
            throw new RuntimeException("Mock setup for RateLimitInterceptor failed", e);
        }
        return interceptor;
    }
}
