package com.ourdressingtable.membercosmetic.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.TestSecurityConfig;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.dto.MemberCosmeticResponse;
import com.ourdressingtable.membercosmetic.service.MemberCosmeticService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = MemberCosmeticController.class)
@DisplayName("회원 화장품 Controller 테스트")
@Import(TestSecurityConfig.class)
public class MemberCosmeticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberCosmeticService memberCosmeticService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원 화장품 생성 테스트")
    class CreateMemberCosmetic {

        @DisplayName("회원 화장품 생성 성공")
        @Test
        public void createdMemberCosmetic_returnSuccess() throws Exception {
            CreateMemberCosmeticRequest request = TestDataFactory.testMemberCosmeticRequest(1L);

            given(memberCosmeticService.createMemberCosmetic(request)).willReturn(1L);

            mockMvc.perform(post("/api/member-cosmetics")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @DisplayName("회원 화장품 생성 실패 - BAD REQUEST")
        @Test
        public void createdMemberCosmetic_returnNotFound() throws Exception {
            CreateMemberCosmeticRequest request = TestDataFactory.testMemberCosmeticRequestWithDressingTableNUll();

            mockMvc.perform(post("/api/member-cosmetics")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("회원 화장품 조회 테스트")
    class GetMemberCosmetic {

        @DisplayName("회원 화장품 조회 성공")
        @Test
        public void getMemberCosmetic_returnSuccess() throws Exception {
            MemberCosmeticResponse response = TestDataFactory.testMemberCosmeticResponse();

            given(memberCosmeticService.getMemberCosmeticDetail(1L)).willReturn(response);

            mockMvc.perform(get("/api/member-cosmetics/{id}",1L)
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.store").value("올리브영"));
        }

        @DisplayName("회원 화장품 조회 실패")
        @Test
        public void getMemberCosmetic_returnNotFoundError() throws Exception {
            MemberCosmeticResponse response = TestDataFactory.testMemberCosmeticResponse();

            doThrow(new OurDressingTableException(ErrorCode.MEMBER_COSMETIC_NOT_FOUND))
                    .when(memberCosmeticService).getMemberCosmeticDetail(100L);

            mockMvc.perform(get("/api/member-cosmetics/{id}",100L)
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }
    }
}
