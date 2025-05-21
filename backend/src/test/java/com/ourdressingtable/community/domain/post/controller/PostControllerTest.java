package com.ourdressingtable.community.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.community.post.controller.PostController;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.exception.ErrorCode;
import com.ourdressingtable.exception.OurDressingTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ActiveProfiles("test")
@WebMvcTest(controllers = PostController.class)
@DisplayName("Post 기능 테스트")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private CommunityService communityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성 테스트")
    class createPost {

        @DisplayName("게시글 작성 - 성공")
        @Test
        public void createPost_withValidData_ReturnSuccess() throws Exception {
            // given
            CreatePostRequest createPostRequest = CreatePostRequest.builder()
                    .title("제목")
                    .content("내용")
                    .communityCategoryId(1L)
                    .build();
            given(postService.createPost(any(),eq(1L))).willReturn(100L);

            // when & then
            mockMvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createPostRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "/api/posts/100"))
                    .andExpect(jsonPath("$.id").value(100));
        }

        @DisplayName("게시글 작성 - 실패")
        @Test
        public void createPost_withInvalidMember_ReturnError() throws Exception {
            // given
            CreatePostRequest createPostRequest = CreatePostRequest.builder()
                    .title("제목")
                    .content("내용")
                    .build();

            given(postService.createPost(any(),eq(1L))).willThrow(new RuntimeException("<UNK>"));

            mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequest)))
                    .andExpect(status().isBadRequest());

        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class updatePost {
        @DisplayName("게시글 수정 - 성공")
        @Test
        public void updatePost_withValidData_ReturnSuccess() throws Exception {
            UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                    .title("수정제목")
                    .content("수정내용")
                    .communityCategoryId(2L)
                    .build();

            mockMvc.perform(patch("/api/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatePostRequest)))
                    .andExpect(status().isNoContent());
        }

        // TODO: 회원 인증 후 수정 필요
        @DisplayName("게시글 수정 - 실패")
        @Test
        public void updatePost_withInvalidMember_ReturnError() throws Exception {
            UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                    .title("수정제목")
                    .content("수정내용")
                    .communityCategoryId(2L)
                    .build();

            doThrow(new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT))
                    .when(postService).updatePost(eq(1L), eq(updatePostRequest));

            mockMvc.perform(patch("/api/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatePostRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

}
