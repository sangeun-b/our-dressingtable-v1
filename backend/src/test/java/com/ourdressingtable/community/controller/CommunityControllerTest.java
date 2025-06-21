package com.ourdressingtable.community.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.TestSecurityConfig;
import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.*;
import com.ourdressingtable.community.post.domain.PostLike;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import org.hamcrest.Matchers;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(controllers = CommunityController.class)
@Import(TestSecurityConfig.class)
@DisplayName("Community 기능 테스트")
public class CommunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityService communityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성 테스트")
    class createPost {

        @DisplayName("게시글 작성 성공")
        @WithCustomUser
        @Test
        public void createPost_withValidData_ReturnSuccess() throws Exception {
            // given
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();
            given(communityService.createPost(any())).willReturn(100L);

            // when & then
            performCreatePost(createPostRequest)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, Matchers.endsWith("/api/community/posts/100")))
                    .andExpect(jsonPath("$.id").value(100));
        }

        @DisplayName("게시글 작성 실패 - 활동 불가 회원")
        @WithCustomUser(status = Status.BLOCK)
        @Test
        public void createPost_withInvalidMember_ReturnError() throws Exception {
            // given
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();

            given(communityService.createPost(any())).willThrow(new OurDressingTableException(
                    ErrorCode.FORBIDDEN));

           performCreatePost(createPostRequest)
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

           performPatchPost(updatePostRequest)
                    .andExpect(status().isNoContent());
        }

        @DisplayName("게시글 수정 실패 - 작성자 불일치")
        @WithCustomUser(memberId = 2L)
        @Test
        public void updatePost_withInvalidMember_ReturnError() throws Exception {
            UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();

            doThrow(new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT))
                    .when(communityService).updatePost(eq(1L), eq(updatePostRequest));

            performPatchPost(updatePostRequest)
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("게시글에 권한이 없습니다."));
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    @WithCustomUser
    class deletePost {

        @DisplayName("게시글 삭제 성공")
        @WithCustomUser
        @Test
        public void deletePost_withValidData_ReturnSuccess() throws Exception {

            performDeletePost(1L)
                    .andExpect(status().isNoContent());

            verify(communityService).deletePost(1L);
        }

        @DisplayName("게시글 삭제 실패 - 미존재 게시물")
        @WithCustomUser
        @Test
        public void deletePost_ReturnPostNotFoundError() throws Exception {
            doThrow(new OurDressingTableException(ErrorCode.POST_NOT_FOUND))
                    .when(communityService).deletePost(eq(1L));

            performDeletePost(1L)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회 테스트")
    class getPost {

        @DisplayName("게시글 상세 조회 성공")
        @WithCustomUser
        @Test
        public void getPost_withValidData_ReturnSuccess() throws Exception {
            // given
            Long postId = 1L;

            PostDetailResponse postDetailResponse = TestDataFactory.testPostDetailResponse();

            given(communityService.getPostDetail(eq(postId))).willReturn(postDetailResponse);

            // when & then
            performGetPostDetail(postId)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("제목"))
                    .andDo(print());

        }

        @DisplayName("게시글 상세 조회 실패 - 미존재 게시물")
        @WithCustomUser
        @Test
        public void getPost_returnPostNotFoundError() throws Exception {
            Long postId = 999L;

            given(communityService.getPostDetail(eq(postId))).willThrow(new OurDressingTableException(ErrorCode.POST_NOT_FOUND));

            performGetPostDetail(postId)
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));

        }
    }

    @Nested
    @DisplayName("게시글 좋아요 등록 테스트")
    class TogglePostLike {

        @DisplayName("게시글 좋아요 등록 성공")
        @WithCustomUser
        @Test
        public void createPostLike_ReturnSuccess() throws Exception {
            // given
            Long postId = 1L;
            Long memberId = 1L;
            given(communityService.toggleLike(postId)).willReturn(true);

            // when & then
            performCreatePostLike(postId, memberId)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.liked").value(true));
        }

        @DisplayName("게시글 좋아요 등록 실패 - 비회원")
        @WithCustomUser
        @Test
        public void createPostLike_ReturnMemberNotActiveError() throws Exception {
            doThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_ACTIVE))
                    .when(communityService).toggleLike(eq(1L));
            Long postId = 1L;
            Long memberId = 1L;
            performCreatePostLike(postId, memberId)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("활동 불가 회원입니다."));;
        }
    }

    @Nested
    @DisplayName("댓글 작성 테스트")
    class CreateComment {

        @DisplayName("댓글 작성 성공")
        @WithCustomUser
        @Test
        public void createComment_returnSuccess() throws Exception {
            // given
            Long commentId = 1L;
            CreateCommentRequest request = TestDataFactory.testCreateCommentRequest(1L);
            Member member = TestDataFactory.testMember(1L);

            given(communityService.createComment(eq(request))).willReturn(commentId);

            performCreateComment(request)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, Matchers.endsWith("/api/community/posts/1/comments/1")))
                    .andExpect(jsonPath("$.id").value(commentId));

        }

        @DisplayName("댓글 작성 실패 - 게시글 미존재")
        @WithCustomUser
        @Test
        public void createComment_returnPostNotFoundError() throws Exception {
            // given
            CreateCommentRequest request = TestDataFactory.testCreateCommentRequestWithNull(null);
            Member member = TestDataFactory.testMember(1L);

            given(communityService.createComment(eq(request))).willThrow(new OurDressingTableException(ErrorCode.POST_NOT_FOUND));

            // when & then
            performCreateComment(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("댓글 삭제 테스트")
    class DeleteComment {

        @DisplayName("댓글 삭제 성공")
        @WithCustomUser
        @Test
        public void deleteComment_returnSuccess() throws Exception {
            // given
            Long commentId = 100L;
            Long postId = 1L;
            // when & then
            performDeleteComment(commentId, postId)
                    .andExpect(status().isNoContent());
        }

        @DisplayName("댓글 삭제 실패 - 댓글 없음")
        @WithCustomUser
        @Test
        public void deleteComment_returnCommentNotFoundError() throws Exception {
            // given
            Long commentId = 999L;
            Long postId = 1L;
            doThrow(new OurDressingTableException(ErrorCode.COMMENT_NOT_FOUND))
                    .when(communityService).deleteComment(commentId);

            // when & then
            performDeleteComment(commentId, postId)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("사용자가 작성한 게시글 조회 테스트")
    class GetMyPosts {

        @DisplayName("사용자 작성 게시글 조회 성공")
        @WithCustomUser
        @Test
        public void getMyPosts_returnSuccess() throws Exception {
            Member member = TestDataFactory.testMember(1L);
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(1L);
            List<PostResponse> postResponseList = List.of(
                    PostResponse.from(TestDataFactory.testPost(1L, member, communityCategory)),
                    PostResponse.from(TestDataFactory.testPost(2L, member, communityCategory))
            );

            Page<PostResponse> responses = new PageImpl<>(postResponseList, PageRequest.of(0, 1), postResponseList.size());

            given(communityService.getMyPosts(any(Pageable.class), any(MyPostSearchCondition.class))).willReturn(responses);

            performGetMyPosts()
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(postResponseList.get(0).getId()));

        }
        @DisplayName("사용자 작성 게시글 조회 실패 - 내부 서버 오류")
        @WithCustomUser
        @Test
        public void getMyPosts_returnInternalServerError() throws Exception {

            given(communityService.getMyPosts(any(Pageable.class), any(MyPostSearchCondition.class)))
                    .willThrow(new OurDressingTableException(ErrorCode.INTERNAL_SERVER_ERROR));

            performGetMyPosts().andExpect(status().isInternalServerError());
        }
    }

        @Nested
        @DisplayName("사용자가 좋아요한 게시글 조회 테스트")
        class GetLikedPosts {

            @DisplayName("사용자 좋아요 게시글 조회 성공")
            @WithCustomUser
            @Test
            public void getLikedPosts_returnSuccess() throws Exception {
                Member member = TestDataFactory.testMember(1L);
                CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(1L);
                Post post = TestDataFactory.testPost(1L, member, communityCategory);
                Post post_second = TestDataFactory.testPost(2L, member, communityCategory);
                List<PostResponse> postResponseList = List.of(
                        PostResponse.from(post),
                        PostResponse.from(post_second)
                );

                PostLike postLike = TestDataFactory.testPostLike(1L, member, post);
                PostLike postLike_second = TestDataFactory.testPostLike(2L, member, post_second);

                Page<PostResponse> responses = new PageImpl<>(postResponseList, PageRequest.of(0, 1), postResponseList.size());

                given(communityService.getLikedPosts(any(Pageable.class), any(MyPostSearchCondition.class))).willReturn(responses);

                performGetLikedPosts()
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].id").value(postResponseList.get(0).getId()));

            }

        @DisplayName("사용자 좋아요 게시글 조회 실패 - 유효하지 않는 게시글")
        @Test
        public void getMyPosts_returnNotFoundError() throws Exception {

            given(communityService.getLikedPosts(any(Pageable.class), any(MyPostSearchCondition.class)))
                    .willThrow(new OurDressingTableException(ErrorCode.POST_NOT_FOUND));

            performGetLikedPosts().andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("사용자가 댓글 단 게시글 조회 테스트")
    class GetCommentedPosts {

        @DisplayName("사용자 댓글 단 게시글 조회 성공")
        @WithCustomUser
        @Test
        public void getCommentedPosts_returnSuccess() throws Exception {
            Member member = TestDataFactory.testMember(1L);
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(1L);
            Post post = TestDataFactory.testPost(1L, member, communityCategory);
            Post post_second = TestDataFactory.testPost(2L, member, communityCategory);
            List<PostResponse> postResponseList = List.of(
                    PostResponse.from(post),
                    PostResponse.from(post_second)
            );



            Comment comment = TestDataFactory.testCommentWithMemberAndPost(1L, member, post);

            Page<PostResponse> responses = new PageImpl<>(postResponseList, PageRequest.of(0, 1), postResponseList.size());

            given(communityService.getCommentedPosts(any(Pageable.class), any(MyPostSearchCondition.class))).willReturn(responses);

            performGetCommentedPosts()
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(postResponseList.get(0).getId()));

        }

        @DisplayName("사용자 댓글 단 게시글 조회 실패 - 내부 서버 오류 ")
        @Test
        public void getMyPosts_returnInternalServerError() throws Exception {

            given(communityService.getCommentedPosts(any(Pageable.class), any(MyPostSearchCondition.class)))
                    .willThrow(new OurDressingTableException(ErrorCode.INTERNAL_SERVER_ERROR));

            performGetCommentedPosts().andExpect(status().isInternalServerError());
        }
    }


    private ResultActions performCreatePost(CreatePostRequest request) throws Exception {
        return mockMvc.perform(post("/api/community/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions performPatchPost(UpdatePostRequest request) throws Exception {
        return mockMvc.perform(patch("/api/community/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions performDeletePost(Long postId) throws Exception {
        return mockMvc.perform(delete("/api/community/posts/{postId}", postId)
                .with(csrf()));
    }

    private ResultActions performGetPostDetail(Long postId) throws Exception {
        return mockMvc.perform(get("/api/community/posts/{postId}", postId)
                .with(csrf()));
    }

    private ResultActions performCreatePostLike(Long postId, Long memberId) throws Exception {
        return mockMvc.perform(post("/api/community/posts/{postId}/likes", postId)
                .param("memberId", memberId.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performCreateComment(CreateCommentRequest request) throws Exception {
        return mockMvc.perform(post("/api/community/posts/{postId}/comments", request.getPostId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions performDeleteComment(Long commentId, Long postId) throws Exception {
        return mockMvc.perform(delete("/api/community/posts/{postId}/comments/{commentId}", postId, commentId)
                .with(csrf()));
    }

    private ResultActions performGetMyPosts() throws Exception {
        return mockMvc.perform(get("/api/community/posts/me")
                .param("page", "0")
                .param("size", "10"));
    }

    private ResultActions performGetLikedPosts() throws Exception {
        return mockMvc.perform(get("/api/community/posts/me/likes")
                .param("page", "0")
                .param("size", "10"));
    }

    private ResultActions performGetCommentedPosts() throws Exception {
        return mockMvc.perform(get("/api/community/posts/me/comments")
                .param("page", "0")
                .param("size", "10"));
    }
}
