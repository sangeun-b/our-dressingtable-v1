package com.ourdressingtable.community.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.communityCategory.dto.CommunityCategoryResponse;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

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

    @Nested
    @DisplayName("게시글 작성 테스트")
    class createPost {
        @DisplayName("게시글 작성 성공")
        @Test
        public void createPosts_shouldReturnSuccess() {
            // given
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();

            CommunityCategory category = CommunityCategory.builder().id(1L).name("자유").build();

            Member member = TestDataFactory.testMember(1L);
            given(memberService.getActiveMemberEntityById(member.getId())).willReturn(member);


            given(communityCategoryService.getCategoryById(1L)).willReturn(CommunityCategoryResponse.from(category));
            given(postService.createPost(createPostRequest,member.getId())).willReturn(123L);

            // when
            Long postId = communityService.createPost(createPostRequest, member.getId());

            // then
            assertEquals(123L, postId);
            then(postService).should().createPost(createPostRequest,1L);
        }

        @DisplayName("게시글 작성 실패_비회원")
        @Test
        public void createPosts_shouldReturnError() {
            // given
            Long invalidMemberId = 999L;
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();

            given(memberService.getActiveMemberEntityById(invalidMemberId)).willReturn(null);
            // when & then
            assertThrows(OurDressingTableException.class, () -> communityService.createPost(createPostRequest, invalidMemberId));
        }

    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class updatePost {
        @DisplayName("게시글 수정 성공")
        @Test
        public void updatePost_shouldReturnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);

            Post post = TestDataFactory.testPost(1L, member);

            UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();

            given(postService.getValidPostEntityById(1L)).willReturn(post);

            // when
            communityService.updatePost(1L, 1L, updatePostRequest);

            // then
            then(postService).should().updatePost(1L, updatePostRequest);


        }

        @DisplayName("게시글 수정 실패_작성자 미일치")
        @Test
        public void updatePost_shouldReturnError() {
            // given
            Member member = TestDataFactory.testMember(1L);
            Post post = TestDataFactory.testPost(1L, member);

            UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();

            given(postService.getValidPostEntityById(1L)).willReturn(post);

            // when & then
            assertThrows(OurDressingTableException.class, () -> {
                communityService.updatePost(1L, 2L, updatePostRequest);
            });
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class deletePost {
        @DisplayName("게시글 삭제 성공")
        @Test
        public void deletePost_shouldReturnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);
            Post post = TestDataFactory.testPost(1L, member);

            given(postService.getValidPostEntityById(1L)).willReturn(post);

            // when
            communityService.deletePost(1L,1L);

            // then
            verify(postService).deletePost(1L);
        }

        @DisplayName("게시글 삭제 실패 - 작성자 불일치")
        @Test
        public void deletePost_shouldReturnNoPermissionError () {
            Member member = TestDataFactory.testMember(1L);
            Post post = TestDataFactory.testPost(1L, member);

            given(postService.getValidPostEntityById(1L)).willReturn(post);

            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> {
                communityService.deletePost(1L, 2L);
            });

            assertEquals(ErrorCode.NO_PERMISSION_TO_EDIT.getCode(), exception.getCode());
        }
    }

}
