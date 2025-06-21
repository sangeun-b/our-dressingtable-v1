package com.ourdressingtable.community.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.common.util.SecurityUtilMockHelper;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.domain.PostLike;
import com.ourdressingtable.community.post.dto.*;
import com.ourdressingtable.community.post.service.PostLikeService;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.communityCategory.dto.CommunityCategoryResponse;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.security.dto.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Community 테스트")
public class CommunityServiceImplTest {

    @InjectMocks
    private CommunityServiceImpl communityService;

    @Mock
    private PostService postService;

    @Mock
    private CommunityCategoryService communityCategoryService;

    @Mock
    private MemberService memberService;

    @Mock
    private PostLikeService postLikeService;

    private Member member;
    private CommunityCategory communityCategory;
    private Post post;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        member = TestDataFactory.testMember(1L);
        communityCategory = TestDataFactory.testCommunityCategory(1L);
        post = TestDataFactory.testPost(1L, member, communityCategory);

        CustomUserDetails userDetails = CustomUserDetails.builder()
                .memberId(1L)
                .email("test@example.com")
                .password("password")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVE)
                .build();
;

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Nested
    @DisplayName("게시글 작성 테스트")
    class createPost {
        @DisplayName("게시글 작성 성공")
        @Test
        public void createPost_shouldReturnSuccess() {
            // given
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();

            given(memberService.getActiveMemberEntityById(1L)).willReturn(member);
            given(communityCategoryService.getCategoryById(1L)).willReturn(CommunityCategoryResponse.from(communityCategory));
            given(postService.createPost(createPostRequest, member.getId())).willReturn(123L);

            // when
            Long postId = communityService.createPost(createPostRequest);

            // then
            assertEquals(123L, postId);
            then(postService).should().createPost(createPostRequest, 1L);
        }

        @DisplayName("게시글 작성 실패_비회원")
        @Test
        public void createPost_shouldReturnError() {
            // given
            Long invalidMemberId = 999L;
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();

            try (MockedStatic<SecurityUtil> mockedSecurityUtil = SecurityUtilMockHelper.mockCurrentMemberIdThrows(new OurDressingTableException(ErrorCode.UNAUTHORIZED))) {

                // when & then
                assertThatThrownBy(() -> communityService.createPost(createPostRequest))
                        .isInstanceOf(OurDressingTableException.class)
                        .hasMessageContaining(ErrorCode.UNAUTHORIZED.getMessage());

            }
        }

    }

        @Nested
        @DisplayName("게시글 수정 테스트")
        class updatePost {
            @DisplayName("게시글 수정 성공")
            @Test
            public void updatePost_shouldReturnSuccess() {
                // given
                UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();

                given(postService.getValidPostEntityById(1L)).willReturn(post);
                given(memberService.getActiveMemberEntityById(1L)).willReturn(member);

                // when
                communityService.updatePost(1L, updatePostRequest);

                // then
                then(postService).should().updatePost(1L, updatePostRequest);


            }

            @DisplayName("게시글 수정 실패_작성자 미일치")
            @Test
            public void updatePost_shouldReturnError() {
                // given
                UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();

                try(MockedStatic<SecurityUtil> mockedSecurityUtil = SecurityUtilMockHelper.mockCurrentMemberIdThrows(new OurDressingTableException(ErrorCode.UNAUTHORIZED))) {

                    // when & then
                    assertThatThrownBy(() -> communityService.updatePost(1L, updatePostRequest))
                            .isInstanceOf(OurDressingTableException.class)
                            .hasMessageContaining(ErrorCode.UNAUTHORIZED.getMessage());
                }
            }
        }

        @Nested
        @DisplayName("게시글 삭제 테스트")
        class deletePost {
            @DisplayName("게시글 삭제 성공")
            @Test
            public void deletePost_shouldReturnSuccess() {
                // given
                given(postService.getValidPostEntityById(1L)).willReturn(post);
                given(memberService.getActiveMemberEntityById(1L)).willReturn(member);

                // when
                communityService.deletePost(1L);

                // then
                verify(postService).deletePost(1L);
            }

            @DisplayName("게시글 삭제 실패 - 작성자 불일치")
            @Test
            public void deletePost_shouldReturnNoPermissionError() {
                given(memberService.getActiveMemberEntityById(1L)).willReturn(member);

                Post otherMemberPost = TestDataFactory.testPost(1L, TestDataFactory.testMember(2L), communityCategory);
                given(postService.getValidPostEntityById(1L)).willReturn(otherMemberPost);

                OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> {
                    communityService.deletePost(1L);
                });

                assertEquals(ErrorCode.NO_PERMISSION_TO_EDIT.getCode(), exception.getCode());
            }
        }

        @Nested
        @DisplayName("게시글 상세 조회 테스트")
        class getPosts {
            @DisplayName("게시글 상세 조회 성공")
            @Test
            public void getPosts_shouldReturnSuccess() {
                boolean liked = true;

                given(postService.getValidPostEntityById(1L)).willReturn(post);
                given(postLikeService.hasLiked(post.getId(), member.getId())).willReturn(liked);

                PostDetailResponse response = communityService.getPostDetail(post.getId());

                assertEquals(post.getId(), response.getId());
                assertEquals(post.getTitle(), response.getTitle());
            }

            @DisplayName("게시글 상세 조회 실패 - 미존재 게시물")
            @Test
            public void getPosts_shouldReturnError() {
                Long postId = 999L;
                given(postService.getValidPostEntityById(postId)).willThrow(new OurDressingTableException(ErrorCode.POST_NOT_FOUND));
                given(memberService.getActiveMemberEntityById(1L)).willReturn(null);

                assertThatThrownBy(() -> communityService.getPostDetail(postId)).isInstanceOf(OurDressingTableException.class)
                        .hasMessageContaining(ErrorCode.POST_NOT_FOUND.getMessage());

            }
        }

        @Nested
        @DisplayName("사용자가 작성한 게시글 조회 테스트")
        class getMyPosts {
            @DisplayName("사용자가 작성한 게시글 조회 성공")
            @Test
            public void getMyPosts_shouldReturnSuccess() {
                MyPostSearchCondition condition = TestDataFactory.testMyPostSearchCondition("");
                Pageable pageable = PageRequest.of(0, 10);

                Page<Post> postPage = new PageImpl<>(List.of(post), pageable, 1);

                given(memberService.getActiveMemberEntityById(1L)).willReturn(member);
                given(postService.getMyPosts(1L, pageable, condition)).willReturn(postPage.map(PostResponse::from));

                Page<PostResponse> result = communityService.getMyPosts(pageable, condition);

                assertThat(result).isNotNull();
                assertEquals(1, result.getTotalElements());
                assertEquals(post.getId(), result.getContent().get(0).getId());

            }

            @DisplayName("사용자가 작성한 게시글 조회 실패 - 내부 서버 오류")
            @Test
            public void getMyPosts_shouldReturnError() {
                MyPostSearchCondition condition = TestDataFactory.testMyPostSearchCondition("");
                Pageable pageable = PageRequest.of(0, 10);

                given(postService.getMyPosts(1L, pageable, condition)).willThrow(new OurDressingTableException(ErrorCode.INTERNAL_SERVER_ERROR));

                assertThatThrownBy(() -> communityService.getMyPosts(pageable, condition))
                        .isInstanceOf(OurDressingTableException.class)
                        .hasMessageContaining(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
                ;
            }
        }

        @Nested
        @DisplayName("사용자가 좋아요한 게시글 조회 테스트")
        class getLikedPosts {
            @DisplayName("사용자가 좋아요한 게시글 조회 성공")
            @Test
            public void getLikedPosts_shouldReturnSuccess() {
                MyPostSearchCondition condition = TestDataFactory.testMyPostSearchCondition("");
                Pageable pageable = PageRequest.of(0, 10);

                PostLike postLike = TestDataFactory.testPostLike(1L, member, post);

                Page<Post> postPage = new PageImpl<>(List.of(post), pageable, 1);

                given(memberService.getActiveMemberEntityById(1L)).willReturn(member);
                given(postService.getLikedPosts(1L, pageable, condition)).willReturn(postPage.map(PostResponse::from));

                Page<PostResponse> result = communityService.getLikedPosts(pageable, condition);

                assertThat(result).isNotNull();
                assertEquals(1, result.getTotalElements());
                assertEquals(post.getId(), result.getContent().get(0).getId());

            }

            @DisplayName("사용자가 좋아요한 게시글 조회 실패 - 미인증 사용자")
            @Test
            public void getLikedPosts_shouldReturnError_whenUnauthorized() {
                MyPostSearchCondition condition = TestDataFactory.testMyPostSearchCondition("");
                Pageable pageable = PageRequest.of(0, 10);

                try (MockedStatic<SecurityUtil> mockedSecurityUtil = SecurityUtilMockHelper.mockCurrentMemberIdThrows(new OurDressingTableException(ErrorCode.UNAUTHORIZED))) {

                    assertThatThrownBy(() -> communityService.getLikedPosts(pageable, condition))
                            .isInstanceOf(OurDressingTableException.class)
                            .hasMessageContaining(ErrorCode.UNAUTHORIZED.getMessage());
                }
            }
        }

    @Nested
    @DisplayName("사용자가 댓글 작성한 게시글 조회 테스트")
    class getCommentedPosts {
        @DisplayName("사용자가 댓글 작성한 게시글 조회 성공")
        @Test
        public void getCommentedPosts_shouldReturnSuccess() {
            MyPostSearchCondition condition = TestDataFactory.testMyPostSearchCondition("");
            Pageable pageable = PageRequest.of(0, 10);

            Comment comment = TestDataFactory.testCommentWithMemberAndPost(1L, member, post);

            Page<Post> postPage = new PageImpl<>(List.of(post), pageable, 1);

            given(memberService.getActiveMemberEntityById(1L)).willReturn(member);
            given(postService.getCommentedPosts(1L, pageable, condition)).willReturn(postPage.map(PostResponse::from));

            Page<PostResponse> result = communityService.getCommentedPosts(pageable, condition);

            assertThat(result).isNotNull();
            assertEquals(1, result.getTotalElements());
            assertEquals(post.getId(), result.getContent().get(0).getId());

        }

        @DisplayName("사용자가 댓글 작성한 게시글 조회 실패 - 미인증 사용자")
        @Test
        public void getLikedPosts_shouldReturnError_whenUnauthorized() {
            MyPostSearchCondition condition = TestDataFactory.testMyPostSearchCondition("");
            Pageable pageable = PageRequest.of(0, 10);

            try (MockedStatic<SecurityUtil> mockedSecurityUtil = SecurityUtilMockHelper.mockCurrentMemberIdThrows(new OurDressingTableException(ErrorCode.UNAUTHORIZED))) {

                assertThatThrownBy(() -> communityService.getCommentedPosts(pageable, condition))
                        .isInstanceOf(OurDressingTableException.class)
                        .hasMessageContaining(ErrorCode.UNAUTHORIZED.getMessage());
            }
        }
    }

    }

