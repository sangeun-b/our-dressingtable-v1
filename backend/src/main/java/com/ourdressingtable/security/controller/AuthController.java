package com.ourdressingtable.security.controller;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.security.auth.JwtTokenProvider;
import com.ourdressingtable.security.auth.RedisTokenService;
import com.ourdressingtable.security.dto.CustomUserDetails;
import com.ourdressingtable.security.dto.LoginRequest;
import com.ourdressingtable.security.dto.LoginResponse;
import com.ourdressingtable.security.dto.RefreshTokenRequest;
import com.ourdressingtable.security.dto.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisTokenService redisTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow();

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getEmail(), member.getRole());

        String refreshToken = jwtTokenProvider.createRefreshToken(
                member.getEmail(), member.getRole());

        redisTokenService.saveTokenInfo(member.getEmail(), accessToken, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"));

        return ResponseEntity.ok(LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).memberId(member.getId()).build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpServletRequest) {
        String token = request.getRefreshToken();

        if(!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenProvider.getEmail(token);
        boolean isValid = redisTokenService.validate(email, token, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"));

        if(!isValid) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Member member = memberRepository.findByEmail(email).orElseThrow();

        String newAccessToken = jwtTokenProvider.createAccessToken(email, member.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email, member.getRole());

        redisTokenService.saveTokenInfo(email, newRefreshToken, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"));

        return ResponseEntity.ok(TokenResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        return ResponseEntity.ok(member);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if(token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmail(token);
            redisTokenService.deleteTokenInfo(email);
        }
        return ResponseEntity.noContent().build();
    }



}
