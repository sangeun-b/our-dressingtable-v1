package com.ourdressingtable.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.domain.member.controller.MemberController;
import com.ourdressingtable.domain.member.domain.Role;
import com.ourdressingtable.domain.member.dto.CreateMemberRequest;
import com.ourdressingtable.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

    @DisplayName("회원 가입")
    @Test
    public void signupMember_withValidInput_shouldReturnSuccess() throws Exception {
        // given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email("member1@gmail.com")
                .password("password")
                .name("member1")
                .nickname("me")
                .phoneNumber("010-1234-5678")
                .role(Role.ROLE_MEMBER)
                .build();

        Long memberId = 1L;
        when(memberService.createMember(any(CreateMemberRequest.class))).thenReturn(memberId);

        // when & then
        mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","/api/member/1"))
                .andExpect(jsonPath("$.id").value(memberId));

    }



}