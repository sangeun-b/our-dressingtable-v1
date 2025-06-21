package com.ourdressingtable.community.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.security.TestSecurityConfig;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.post.dto.PostResponse;
import com.ourdressingtable.community.post.dto.PostSearchCondition;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.member.domain.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@ActiveProfiles("test")
@WebMvcTest(controllers = PostController.class)
@Import(TestSecurityConfig.class)
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
    @DisplayName("게시글 조회 테스트")
    class GetPosts {
        @DisplayName("게시글 조회 성공")
        @Test
        public void getPosts_returnSuccess() throws Exception {
            Member member = TestDataFactory.testMember(1L);
            CommunityCategory category = TestDataFactory.testCommunityCategory(1L);
            List<PostResponse> postResponseList = List.of(
                    PostResponse.from(TestDataFactory.testPost(1L, member, category)),
                    PostResponse.from(TestDataFactory.testPost(2L, member, category))
            );
            Page<PostResponse> responses = new PageImpl<>(postResponseList, PageRequest.of(0,10), postResponseList.size());

            given(postService.getPosts(any(PostSearchCondition.class), any(Pageable.class)))
                    .willReturn(responses);

            mockMvc.perform(get("/api/posts")
                    .param("keyword", "")
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andExpect(jsonPath("$.content[0].id").value(1));
        }

        @DisplayName("게시글 목록 조회 실패 - 내부 서버 오류")
        @Test
        public void getPosts_returnInternalServerError() throws Exception {
            given(postService.getPosts(any(PostSearchCondition.class), any(Pageable.class)))
                    .willThrow(new OurDressingTableException(ErrorCode.INTERNAL_SERVER_ERROR));

            mockMvc.perform(get("/api/posts")
                    .param("keyword", "테스트")
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isInternalServerError());
        }
    }


}
