package com.ourdressingtable.community.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.community.post.controller.PostLikeController;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.service.PostLikeService;
import com.ourdressingtable.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = PostLikeController.class)
@DisplayName("PostLike 기능 테스트")
public class PostLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostLikeService postLikeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 좋아요 등록 테스트")
    class PostLikeTest{
        @DisplayName("게시글 좋아요 등록 테스트 성공")
        @Test
        public void createPostLike_ReturnSuccess() throws Exception {
            // given
            Post post = Post.builder().id(1L).build();
            Member member = Member.builder().id(1L).build();
            given(postLikeService.toggleLike(post.getId(), member.getId())).willReturn(true);

            // when & then
            mockMvc.perform(post("/api/posts/{postId}/like", post.getId())
                            .param("memberId", member.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.liked").value(true));
        }

        @DisplayName("게시글 좋아요 등록 테스트 실패")
        @Test
        public void createPostLike_ReturnFailure() throws Exception {
            doThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_ACTIVE))
                    .when(postLikeService).toggleLike(eq(1L), eq(1L));
            Long postId = 1L;
            Long memberId = 1L;
            mockMvc.perform(post("/api/posts/{postId}/like", postId)
                    .param("memberId", memberId.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
