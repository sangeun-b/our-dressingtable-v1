package com.ourdressingtable.community.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.post.controller.PostController;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Status;
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
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@ActiveProfiles("test")
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
        @WithCustomUser
        @Test
        public void createPost_withValidData_ReturnSuccess() throws Exception {
            // given
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();
            given(communityService.createPost(any(),eq(1L))).willReturn(100L);

            // when & then
            mockMvc.perform(post("/api/posts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "/api/posts/100"))
                    .andExpect(jsonPath("$.id").value(100));
        }

        @DisplayName("게시글 작성 - 실패")
        @WithCustomUser(status = Status.BLOCK)
        @Test
        public void createPost_withInvalidMember_ReturnError() throws Exception {
            // given
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();

            given(communityService.createPost(any(),eq(1L))).willThrow(new OurDressingTableException(ErrorCode.FORBIDDEN));

            mockMvc.perform(post("/api/posts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequest)))
                    .andExpect(status().isForbidden());

        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class updatePost {
        @DisplayName("게시글 수정 성공")
        @WithCustomUser
        @Test
        public void updatePost_withValidData_ReturnSuccess() throws Exception {
            UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();

            mockMvc.perform(patch("/api/posts/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatePostRequest)))
                    .andExpect(status().isNoContent());
        }

        @DisplayName("게시글 수정 실패 - 작성자 불일치")
        @WithCustomUser(memberId = 2L)
        @Test
        public void updatePost_withInvalidMember_ReturnError() throws Exception {
            UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();

            doThrow(new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT))
                    .when(communityService).updatePost(eq(1L), eq(2L), eq(updatePostRequest));

            mockMvc.perform(patch("/api/posts/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatePostRequest)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    @WithCustomUser
    class deletePost {
        @DisplayName("게시글 삭제 - 성공")
        @Test
        public void deletePost_withValidData_ReturnSuccess() throws Exception {

            mockMvc.perform(delete("/api/posts/1")
                    .with(csrf()))
                    .andExpect(status().isNoContent());

            verify(communityService).deletePost(1L,1L);
        }

        @DisplayName("게시글 삭제 - 실패")
        @WithCustomUser
        @Test
        public void deletePost_withInvalidMember_ReturnError() throws Exception {
            doThrow(new OurDressingTableException(ErrorCode.POST_NOT_FOUND))
                    .when(communityService).deletePost(eq(1L),eq(1L));

            mockMvc.perform(delete("/api/posts/1")
                    .with(csrf()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("게시글 단건 조회 테스트")
    class getPost {
        @DisplayName("게시글 조회 - 성공")
        @WithCustomUser
        @Test
        public void getPost_withValidData_ReturnSuccess() throws Exception {
            // given
            Long postId = 1L;
            Long memberId = 1L;

            PostDetailResponse postDetailResponse = TestDataFactory.testPostDetailResponse();

            given(communityService.getPostDetail(eq(postId), eq(memberId))).willReturn(postDetailResponse);

            // when & then
            mockMvc.perform(get("/api/posts/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
            .andDo(print());

        }
        @DisplayName("게시글 조회 - 실패")
        @WithCustomUser
        @Test
        public void getPost_withInvalidPost_ReturnError() throws Exception {
            Long postId = 999L;
            Long memberId = 1L;

            given(communityService.getPostDetail(eq(postId), eq(memberId))).willThrow(new OurDressingTableException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(get("/api/posts/{postId}", postId)
                    .with(csrf()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));

        }
    }


}
