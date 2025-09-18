package com.ourdressingtable.dressingtable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.TestSecurityConfig;
import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.dressingtable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingtable.dto.DressingTableResponse;
import com.ourdressingtable.dressingtable.dto.UpdateDressingTableRequest;
import com.ourdressingtable.dressingtable.service.DressingTableService;
import java.util.List;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = DressingTableController.class)
@DisplayName("화장대 Controller 테스트")
@Import(TestSecurityConfig.class)
public class DressingTableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DressingTableService dressingTableService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("화장대 생성 테스트")
    class CreateDressingTable {

        @DisplayName("화장대 생성 성공")
        @WithCustomUser
        @Test
        public void createDressingTable_returnSuccess() throws Exception {
            // given
            CreateDressingTableRequest createDressingTableRequest = TestDataFactory.testCreateDressingTableRequest();

            given(dressingTableService.createDressingTable(any())).willReturn(1L);

            // when & then
            mockMvc.perform(post("/api/dressing-tables")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDressingTableRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @DisplayName("화장대 생성 실패 - BAD REQUEST (이름 없음)")
        @WithCustomUser
        @Test
        public void createDressingTable_returnBadRequest() throws Exception {
            CreateDressingTableRequest createDressingTableRequest = TestDataFactory.testCreateDressingTableRequestWithNameNull();

            mockMvc.perform(post("/api/dressing-tables")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDressingTableRequest)))
                    .andExpect(status().isBadRequest());
        }


    }

    @Nested
    @DisplayName("화장대 수정 테스트")
    class UpdateDressingTable {
        @DisplayName("화장대 수정 성공")
        @WithCustomUser
        @Test
        public void updateDressingTable_returnSuccess() throws Exception {
            // given
            UpdateDressingTableRequest updateDressingTableRequest = TestDataFactory.testUpdateDressingTableRequest();

            doNothing().when(dressingTableService)
                    .updateDressingTable(any(UpdateDressingTableRequest.class),anyLong());

            // when & then
            mockMvc.perform(patch("/api/dressing-tables/{id}", 1L)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDressingTableRequest)))
                    .andExpect(status().isNoContent());
        }

        @DisplayName("화장대 수정 실패")
        @WithCustomUser
        @Test
        public void updateDressingTable_returnNotFound() throws Exception {
            UpdateDressingTableRequest updateDressingTableRequest = TestDataFactory.testUpdateDressingTableRequest();

            doThrow(new OurDressingTableException(ErrorCode.DRESSING_TABLE_NOT_FOUND))
                    .when(dressingTableService).updateDressingTable(any(UpdateDressingTableRequest.class),eq(999L));

            mockMvc.perform(patch("/api/dressing-tables/{id}", 999L)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDressingTableRequest)))
                    .andExpect(status().isNotFound());
        }
    }
    @Nested
    @DisplayName("화장대 삭제 테스트")
    class DeleteDressingTable {

        @DisplayName("화장대 삭제 성공")
        @WithCustomUser
        @Test
        public void deleteDressingTable_returnSuccess() throws Exception {
            // given
            doNothing().when(dressingTableService).deleteDressingTable(eq(1L));

            // when & then
            mockMvc.perform(delete("/api/dressing-tables/{id}", 1L)
                    .with(csrf()))
                    .andExpect(status().isNoContent());

            verify(dressingTableService).deleteDressingTable(1L);
        }

        @DisplayName("화장대 삭제 실패 - 권한 없음")
        @WithCustomUser
        @Test
        public void deleteDressingTable_returnForbidden() throws Exception {
            doThrow(new OurDressingTableException(ErrorCode.FORBIDDEN))
                    .when(dressingTableService).deleteDressingTable(eq(1L));

            mockMvc.perform(delete("/api/dressing-tables/{id}", 1L)
                    .with(csrf()))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("나의 모든 화장대 조회 테스트")
    class GetAllMyDressingTable {

        @DisplayName("나의 모든 화장대 조회 성공")
        @WithCustomUser
        @Test
        public void getAllMyDressingTable_returnSuccess() throws Exception {
            DressingTableResponse firstDressingTableResponse = TestDataFactory.testDressingTableResponse(1L, "첫번째 화장대");
            DressingTableResponse secondDressingTableResponse = TestDataFactory.testDressingTableResponse(2L, "두번째 화장대");
            List<DressingTableResponse> dressingTableResponseList = List.of(firstDressingTableResponse, secondDressingTableResponse);

            given(dressingTableService.getAllMyDressingTables()).willReturn(dressingTableResponseList);

            performGetAllMyDressingTable()
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }

        @DisplayName("나의 모든 화장대 조회 실패 - 미인증 사용자")
        @Test
        public void getAllMyDressingTable_returnMemberError() throws Exception {
            mockMvc.perform(get("/api/dressing-tables/mine")
                    .with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }

    private ResultActions performGetAllMyDressingTable() throws Exception {
        return mockMvc.perform(get("/api/dressing-tables/mine")
                .with(csrf()));
    }
}
