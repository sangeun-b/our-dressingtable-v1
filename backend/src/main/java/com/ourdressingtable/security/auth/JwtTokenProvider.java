package com.ourdressingtable.security.auth;

import com.ourdressingtable.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.access-token-validity}")
    private long accessTokenValidity;

    @Value("${spring.jwt.refresh-token-validity")
    private long refreshTokenValidity;

    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider secretKey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));

        log.info("[init] JwtPROVIDER secretKey 초기화 완료");
    }

    public String createAccessToken(String email, Role role) {
        log.info("[createAccessToken] 토큰 생성 시작");
        return createToken(email, role.name(), accessTokenValidity);
    }

    public String createRefreshToken(String email, Role role) {
        return createToken(email, null, refreshTokenValidity);
    }

    private String createToken(String email, String role, long validity) {
        Claims claims = Jwts.claims().setSubject(email);
        if(role != null)
            claims.put("role", role);
        Date now = new Date();
        Date validityDate = new Date(now.getTime() + validity);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validityDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("[validateToken] 토큰 체크 예외 발생");
            return false;
        }
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role");
    }

//    public Authentication getAuthentication(String token) {
//        String email = getEmail(token);
//        String role = getRole(token);
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_"+role));
//        return new UsernamePasswordAuthenticationToken(email, null, authorities);
//    }

    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
