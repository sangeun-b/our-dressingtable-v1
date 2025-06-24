package com.ourdressingtable.auth.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.auth.dto.FindEmailRequest;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.MaskingUtil;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.auth.service.JwtTokenProvider;
import com.ourdressingtable.auth.service.RedisTokenService;
import com.ourdressingtable.auth.email.dto.ConfirmEmailVerificationCodeRequest;
import com.ourdressingtable.auth.email.dto.SendEmailVerificationCodeRequest;
import com.ourdressingtable.auth.email.service.EmailVerificationService;
import com.ourdressingtable.auth.email.service.ResetPasswordEmailService;
import com.ourdressingtable.auth.controller.AuthController;
import com.ourdressingtable.auth.dto.LoginRequest;
import com.ourdressingtable.auth.email.dto.ConfirmPasswordResetRequest;
import com.ourdressingtable.auth.dto.RefreshTokenRequest;
import com.ourdressingtable.auth.email.dto.ResetPasswordEmailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Spring security API 테스트")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberService memberService;

    @MockBean
    private RedisTokenService redisTokenService;

    @MockBean
    private EmailVerificationService emailVerificationService;

    @MockBean
    private ResetPasswordEmailService resetPasswordEmailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("로그인 API 테스트")
    class LoginTest {
        @DisplayName("로그인 성공")
        @Test
        public void login_shouldReturnSuccess() throws Exception {
            Member member = TestDataFactory.testMember(1L);
            LoginRequest loginRequest = TestDataFactory.testLoginRequest();

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), null, List.of()));

            given(memberService.getActiveMemberEntityByEmail(anyString())).willReturn(member);
            given(jwtTokenProvider.createAccessToken(anyString(), any())).willReturn("access-token");
            given(jwtTokenProvider.createRefreshToken(anyString(), any())).willReturn("refresh-token");

            mockMvc.perform(post("/api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("access-token"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
        }

        @DisplayName("로그인 실패")
        @Test
        public void login_shouldReturnUnauthorized() throws Exception {
            LoginRequest loginRequest = TestDataFactory.testLoginRequest();

            given(authenticationManager.authenticate(any())).willThrow(new OurDressingTableException(ErrorCode.UNAUTHORIZED));

            mockMvc.perform(post("/api/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("refresh token API 테스트")
    class RefreshTokenTest {
        @DisplayName("token 발급 성공 - 유효한 refreshToken이면 새로운 토큰 반환")
        @Test
        public void refreshToken_shouldReturnSuccess() throws Exception {
            String refreshToken = "refresh-token";
            String newAccessToken = "new-accessToken";
            String newRefreshToken = "new-refresh-token";

            RefreshTokenRequest request = RefreshTokenRequest.builder().refreshToken(refreshToken).build();
            Member member = TestDataFactory.testMember(1L);

            given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmail(refreshToken)).willReturn(member.getEmail());
            given(redisTokenService.validate(eq(member.getEmail()), eq(refreshToken), any(), any(), eq("refreshToken"))).willReturn(true);
            given(memberService.getActiveMemberEntityByEmail(anyString())).willReturn(member);
            given(jwtTokenProvider.createAccessToken(anyString(), any())).willReturn(newAccessToken);
            given(jwtTokenProvider.createRefreshToken(anyString(), any())).willReturn(newRefreshToken);

            mockMvc.perform(post("/api/auth/refresh")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                    .andExpect(jsonPath("$.refreshToken").value(newRefreshToken));
        }

        @DisplayName("token 발급 실패 - 유효하지 않은 token")
        @Test
        public void refreshToken_shouldReturnUnauthorized() throws Exception {
            String refreshToken = "refresh-token";
            RefreshTokenRequest request = RefreshTokenRequest.builder().refreshToken(refreshToken).build();

            given(jwtTokenProvider.validateToken(refreshToken)).willReturn(false);

            mockMvc.perform(post("/api/auth/refresh")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("로그아웃 API 테스트")
    class logoutTest {
        @DisplayName("로그아웃 성공")
        @Test
        public void logout_shouldReturnSuccess() throws Exception {
            String accessToken = "access-token";
            String email = "test@example.com";
            long remainingTime = 3600000L;

            given(jwtTokenProvider.resolveToken(any())).willReturn(accessToken);
            given(jwtTokenProvider.validateToken(accessToken)).willReturn(true);
            given(jwtTokenProvider.getExpirationTime(accessToken)).willReturn(remainingTime);
            given(jwtTokenProvider.getEmail(accessToken)).willReturn(email);

            mockMvc.perform(post("/api/auth/logout")
                    .with(csrf()))
                    .andExpect(status().isNoContent());

            verify(redisTokenService).blacklistAccessToken(accessToken, remainingTime);
            verify(redisTokenService).deleteTokenInfo(eq(email), eq("refreshToken"), any());
        }

        @DisplayName("로그아웃 실패")
        @Test
        public void logout_shouldReturnUnauthorized() throws Exception {
            String accessToken = "access-token";

            given(jwtTokenProvider.resolveToken(any())).willReturn(accessToken);
            given(jwtTokenProvider.validateToken(accessToken)).willReturn(false);

            mockMvc.perform(post("/api/auth/logout")
                    .with(csrf()))
                    .andExpect(status().isNoContent());

            verify(redisTokenService, never()).blacklistAccessToken(any(), anyLong());
            verify(redisTokenService, never()).deleteTokenInfo(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("이메일 인증코드 전송 API 테스트")
    class SendEmailVerificationTest {
        @DisplayName("이메일 인증코드 전송 성공")
        @Test
        public void sendVerification_returnSuccess() throws Exception {
            SendEmailVerificationCodeRequest request = TestDataFactory.testSendEmailVerificationCodeRequest();

            mockMvc.perform(post("/api/auth/verification-code/email")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("이메일 인증코드 확인 API 테스트")
    class ConfirmEmailVerificationTest {
        @DisplayName("이메일 인증코드 확인 성공")
        @Test
        public void confirmVerification_returnSuccess() throws Exception {
            ConfirmEmailVerificationCodeRequest request = TestDataFactory.testConfirmEmailVerificationCodeRequest ();

            mockMvc.perform(post("/api/auth/confirm-verification-code/email")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @DisplayName("이메일 인증코드 확인 실패 - 인증 코드 불일치")
        @Test
        public void confirmVerification_returnBadRequestError() throws Exception {
            ConfirmEmailVerificationCodeRequest request = TestDataFactory.testConfirmEmailVerificationCodeRequest ();

            doThrow(new OurDressingTableException(ErrorCode.INVALID_VERIFICATION_CDOE))
                    .when(emailVerificationService).confirmVerification(request.getEmail(), request.getVerificationCode());

            mockMvc.perform(post("/api/auth/confirm-verification-code/email")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("비밀번호 재설정 요청 API 테스트")
    class PasswordResetTest {

        @DisplayName("비밀번호 재설정 요청 성공")
        @Test
        public void resetPasswordRequest_returnSuccess() throws Exception {
            ResetPasswordEmailRequest request = TestDataFactory.testResetPasswordEmailRequest("test@example.com");

            mockMvc.perform(post("/api/auth/reset-password/request")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @DisplayName("비밀번호 재설정 요청 실패 - 존재하지 않는 이메일")
        @Test
        public void resetPasswordRequest_returnEmailNotVerifiedError() throws Exception {
            String email = "notverifiedemail@example.com";

            willThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND))
                    .given(resetPasswordEmailService)
                    .sendResetLink(email);

            ResetPasswordEmailRequest request = TestDataFactory.testResetPasswordEmailRequest(email);

            mockMvc.perform(post("/api/auth/reset-password/request")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }


    }

    @Nested
    @DisplayName("비밀번호 재설정 API 테스트")
    class PasswordResetConfirmTest {

        @DisplayName("비밀번호 재설정 성공")
        @Test
        public void resetPassword_returnSuccess() throws Exception {
            ConfirmPasswordResetRequest request = TestDataFactory.testPasswordResetRequest("valid-token","newPassword123!");
            mockMvc.perform(post("/api/auth/reset-password/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @DisplayName("비밀번호 재설정 실패 - 만료된 토큰")
        @Test
        public void confirmResetPassword_returnSuccess() throws Exception {
            String token = "expired-token";
            String password = "newPassword123!!!!!";

            willThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND))
                    .given(resetPasswordEmailService)
                    .resetPassword(token, password);

            ConfirmPasswordResetRequest request = TestDataFactory.testPasswordResetRequest(token, password);

            mockMvc.perform(post("/api/auth/reset-password/confirm")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }


    }

    @Nested
    @DisplayName("이메일(ID) 찾기 API 테스트")
    class FindEmailTest {

        @DisplayName("이메일(ID) 찾기 성공")
        @Test
        public void findEmailById_returnSuccess() throws Exception {
            FindEmailRequest request = TestDataFactory.testFindEmailRequest("김이름", "010-1234-5678");
            String expectedEmail = "test@example.com";
            String maskedEmail = MaskingUtil.maskedEmail(expectedEmail);

            given(memberService.getEmailByNameAndPhone(request.getName(), request.getPhoneNumber()))
                    .willReturn(expectedEmail);

            mockMvc.perform(post("/api/auth/find-email")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(maskedEmail));
        }

        @DisplayName("이메일(ID) 찾기 실패 - 존재하지 않는 회원")
        @Test
        public void findEmailById_returnMemberNotFoundError() throws Exception {
            FindEmailRequest request = TestDataFactory.testFindEmailRequest("비회원", "010-1234-5678");

            given(memberService.getEmailByNameAndPhone(request.getName(), request.getPhoneNumber()))
                    .willThrow(new OurDressingTableException(ErrorCode.MEMBER_EMAIL_NOT_FOUND));

            mockMvc.perform(post("/api/auth/find-email")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("가입된 이메일이 존재하지 않거나 확인할 수 없습니다."));

        }

        @DisplayName("이메일(ID) 찾기 실패 - 이름 미입력")
        @Test
        public void findEmailById_returnBadRequestError() throws Exception {
            FindEmailRequest request = TestDataFactory.testFindEmailRequest("", "010-1234-5678");

            mockMvc.perform(post("/api/auth/find-email")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

        }
    }
}
