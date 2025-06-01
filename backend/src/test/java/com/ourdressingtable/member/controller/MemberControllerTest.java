package com.ourdressingtable.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.willThrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.SkinType;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ActiveProfiles("test")
@WebMvcTest(controllers = MemberController.class)
@DisplayName("회원 기능 테스트")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원 가입 - 성공")
    @Test
    public void signupMember_withValidInput_shouldReturnSuccess() throws Exception {
        // given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email("member1@gmail.com")
                .password("Password1234!!")
                .name("member1")
                .nickname("me")
                .phoneNumber("010-1234-5678")
                .build();

        Long memberId = 1L;
        when(memberService.createMember(any(CreateMemberRequest.class))).thenReturn(memberId);

        // when & then
        mockMvc.perform(post("/api/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","/api/members/1"))
                .andExpect(jsonPath("$.id").value(memberId));

    }

    @DisplayName("회원 가입 - 실패")
    @Test
    public void signupMember_withInvalidInput_shouldReturnError() throws Exception {
        // given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email("member1@gmail.com")
                .password("password")
                .build();

        Long memberId = 1L;
        when(memberService.createMember(any(CreateMemberRequest.class))).thenReturn(memberId);

        // when & then
        mockMvc.perform(post("/api/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @DisplayName("다른 회원 정보 조회 - 성공")
    @Test
    public void getOtherMember_shouldReturnSuccess() throws Exception {
        //given
        OtherMemberResponse otherMemberResponse = OtherMemberResponse.builder()
                .nickname("me")
                .skinType(SkinType.OILY_SKIN)
                .build();
        //when
        when(memberService.getMember(1L)).thenReturn(otherMemberResponse);
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/1")).andExpect(status().isOk()).andDo(print());

    }

    @DisplayName("다른 회원 정보 조회 - 실패")
    @Test
    public void getOtherMember_shouldReturnError() throws Exception {
        // given
        Long memberId = 99L;
        when(memberService.getMember(memberId)).thenThrow(new OurDressingTableException(
                ErrorCode.MEMBER_NOT_FOUND));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/{id}", memberId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }

    @DisplayName("회원 정보 수정 - 성공")
    @Test
    public void updateMember_shouldReturnSuccess() throws Exception {
        // given
        Long memberId = 1L;
        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
                .nickname("new me")
                .phoneNumber("010-1234-5678")
                .build();

        // when
        doNothing().when(memberService).updateMember(memberId,updateMemberRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateMemberRequest)))
                .andExpect(status().isNoContent()).andDo(print());
    };

}