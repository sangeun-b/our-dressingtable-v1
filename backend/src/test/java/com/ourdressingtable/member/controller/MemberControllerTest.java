package com.ourdressingtable.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.willThrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.TestSecurityConfig;
import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.dto.request.CreateMemberRequest;
import com.ourdressingtable.member.dto.response.MemberResponse;
import com.ourdressingtable.member.dto.response.OtherMemberResponse;
import com.ourdressingtable.member.dto.request.UpdateMemberRequest;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = MemberController.class)
@Import(TestSecurityConfig.class)
@DisplayName("회원 기능 테스트")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원 가입 테스트")
    class createMemberTest{
        @DisplayName("회원 가입 성공")
        @Test
        @WithAnonymousUser
        public void signupMember_withValidInput_returnSuccess() throws Exception {
            // given
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();
            Member member = TestDataFactory.testMember(1L);
            given(memberService.createMember(any(CreateMemberRequest.class))).willReturn(member.getId());

            // when & then
            mockMvc.perform(post("/api/members/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createMemberRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location","/api/members/1"))
                    .andExpect(jsonPath("$.id").value(member.getId()));

        }

        @DisplayName("회원 가입 실패")
        @Test
        @WithAnonymousUser
        public void signupMember_withInvalidInput_returnError() throws Exception {
            // given
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();

            Member member = TestDataFactory.testMember(1L);
            when(memberService.createMember(any(CreateMemberRequest.class))).thenThrow(new OurDressingTableException(ErrorCode.EMAIL_ALREADY_EXISTS));

            // when & then
            mockMvc.perform(post("/api/members/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createMemberRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict());

        }
    }

    @Nested
    @DisplayName("다른 회원 정보 조회 테스트")
    class getMemberTest {
        @DisplayName("다른 회원 정보 조회 성공")
        @WithCustomUser
        @Test
        public void getOtherMember_returnSuccess() throws Exception {
            //given
            OtherMemberResponse otherMemberResponse = TestDataFactory.testOtherMemberResponse();
            //when
            when(memberService.getOtherMember(1L)).thenReturn(otherMemberResponse);
            //then
            mockMvc.perform(get("/api/members/1")
                    .with(csrf())).andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.nickname").value("me"));

        }

        @DisplayName("다른 회원 정보 조회 실패")
        @WithCustomUser
        @Test
        public void getOtherMember_returnError() throws Exception {
            // given
            Long memberId = 99L;
            when(memberService.getOtherMember(memberId)).thenThrow(new OurDressingTableException(
                    ErrorCode.MEMBER_NOT_FOUND));

            // when & then
            mockMvc.perform(get("/api/members/{id}", memberId).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("회원 정보 수정 테스트")
    class updateMember {
        @DisplayName("회원 정보 수정 성공")
        @WithCustomUser
        @Test
        public void updateMember_returnSuccess() throws Exception {
            // given
            UpdateMemberRequest updateMemberRequest = TestDataFactory.testUpdateMemberRequest();

            // when
            doNothing().when(memberService).updateMember(updateMemberRequest);

            // then
            mockMvc.perform(patch("/api/members/my-information")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateMemberRequest)))
                    .andExpect(status().isNoContent()).andDo(print());
        };

        @DisplayName("회원 정보 수정 실패 - 탈퇴한 회원")
        @WithCustomUser(status = Status.WITHDRAWAL)
        @Test
        public void updateMember_returnError () throws Exception{
            //given
            UpdateMemberRequest updateMemberRequest = TestDataFactory.testUpdateMemberRequest();

            //when
            willThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_ACTIVE))
                    .given(memberService).updateMember(any(UpdateMemberRequest.class));

            //then
            mockMvc.perform(patch("/api/members/my-information")
            .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateMemberRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ErrorCode.MEMBER_NOT_ACTIVE.getMessage()))
                    .andExpect(jsonPath("$.code").value(ErrorCode.MEMBER_NOT_ACTIVE.getCode()))
                    .andDo(print());

        }


    }

    @Nested
    @DisplayName("회원 삭제 테스트")
    class deleteMember {
        @DisplayName("회원 삭제 성공")
        @WithCustomUser
        @Test
        public void deleteMember_returnSuccess() throws Exception {
            // given
            Long memberId = 1L;
            WithdrawalMemberRequest withdrawalMemberRequest = TestDataFactory.testWithdrawalMemberRequest();

            mockMvc.perform(delete("/api/members/my-account")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawalMemberRequest)))
                    .andExpect(status().isNoContent()).andDo(print());

            verify(memberService).withdrawMember(any(WithdrawalMemberRequest.class));

        };

        @DisplayName("회원 삭제 실패 - 이미 탈퇴한 회원")
        @WithCustomUser(status = Status.WITHDRAWAL)
        @Test
        public void deleteMember_returnError () throws Exception{
            WithdrawalMemberRequest withdrawalMemberRequest = TestDataFactory.testWithdrawalMemberRequest();

            willThrow(new OurDressingTableException(ErrorCode.ALREADY_WITHDRAW_OR_BLOCKED))
                    .given(memberService).withdrawMember(any(WithdrawalMemberRequest.class));

            mockMvc.perform(delete("/api/members/my-account")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(withdrawalMemberRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ErrorCode.ALREADY_WITHDRAW_OR_BLOCKED.getMessage()))
                    .andDo(print());
        }

    }
    @Nested
    @DisplayName("내 정보 조회 테스트")
    class GetMyInformation {
        @DisplayName("내 정보 조회 성공")
        @WithCustomUser
        @Test
        public void getMyInformation_returnSuccess() throws Exception {
            MemberResponse memberResponse = TestDataFactory.testMemberResponse();

            when(memberService.getMyInfo()).thenReturn(memberResponse);

            mockMvc.perform(get("/api/members/my-information")
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(memberResponse.getName()))
                    .andExpect(jsonPath("$.email").value(memberResponse.getEmail()));
        }

        @DisplayName("내 정보 조회 실패 - 탈퇴 회원")
        @WithCustomUser(status = Status.WITHDRAWAL)
        @Test
        public void getMyInformation_returnError () throws Exception {
            when(memberService.getMyInfo())
                    .thenThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_ACTIVE));

            mockMvc.perform(get("/api/members/my-information")
                    .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ErrorCode.MEMBER_NOT_ACTIVE.getMessage()));
        }
    }



}
