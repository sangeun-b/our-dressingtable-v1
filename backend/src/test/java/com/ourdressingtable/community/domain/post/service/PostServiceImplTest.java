package com.ourdressingtable.community.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.repository.PostRepository;
import com.ourdressingtable.community.post.service.PostServiceImpl;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.common.exception.ErrorCode;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommunityCategoryService communityCategoryService;

    @Mock
    private MemberService memberService;

    @Nested
    @DisplayName("게시글 작성 테스트")
    class createPost {
        @DisplayName("게시글 작성_성공")
        @Test
        public void createPost_shouldReturnSuccess() {
            // given
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(10L);

            Member member = TestDataFactory.testMember(1L);
            given(memberService.getMemberEntityById(member.getId())).willReturn(member);

            given(communityCategoryService.getCategoryEntityById(10L)).willReturn(communityCategory);
            given(postRepository.save(any(Post.class))).willAnswer(invocation -> {
                Post saved = invocation.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", 100L);
                return saved;
            });

            // when
            Long postId = postService.createPost(createPostRequest, member.getId());

            // then
            assertEquals(100L, postId);
            then(postRepository).should().save(any(Post.class));

        }

        @DisplayName("게시글 작성 실패 - MEMBER NOT FOUND")
        @Test
        public void createPost_shouldReturnUserNotFoundError() {
            // given
            Long memberId = 999L;
            CreatePostRequest createPostRequest = TestDataFactory.testCreatePostRequest();

            given(memberService.getActiveMemberEntityById(memberId)).willThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));

            assertThrows(OurDressingTableException.class, () -> postService.createPost(createPostRequest, memberId));
        }
    }
    @Nested
    @DisplayName("게시글 수정 테스트")
    class updatePost {
        @DisplayName("게시글 수정 성공")
        @Test
        public void updatePost_shouldReturnSuccess() {
            UpdatePostRequest updatePostRequest = TestDataFactory.testUpdatePostRequest();
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(10L);
            Member member = TestDataFactory.testMember(1L);
            Post post = TestDataFactory.testPost(1L,member, communityCategory);

            ReflectionTestUtils.setField(post, "id", 1L);

            CommunityCategory newCategory = CommunityCategory.builder().id(2L).name("기타").build();

            given(communityCategoryService.getCategoryEntityById(2L)).willReturn(newCategory);
            given(postRepository.findById(1L)).willReturn(Optional.of(post));

            // when
            postService.updatePost(1L, updatePostRequest);

            // then
            assertEquals(2L, post.getCommunityCategory().getId());
            assertEquals("수정 제목", post.getTitle());
            assertEquals("수정 내용", post.getContent());
        }

        @DisplayName("게시글 수정 실패 - POST NOT FOUND")
        @Test
        public void updatePost_shouldReturnPostNotFoundError() {
            // given
            given(postRepository.findById(2L)).willReturn(Optional.empty());

            // when & then
            assertThrows(OurDressingTableException.class, () -> postService.updatePost(2L, UpdatePostRequest.builder().build()));
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class deletePost {
        @DisplayName("게시글 삭제 성공")
        @Test
        public void deletePost_shouldReturnSuccess() {
            // given
            Post post = Post.builder()
                    .id(1L)
                    .title("제목")
                    .isDeleted(false)
                    .member(Member.builder().id(1L).build())
                    .build();

            given(postRepository.findById(1L)).willReturn(Optional.of(post));

            // when
            postService.deletePost(1L);

            // then
            assertThat(post.isDeleted()).isTrue();
        }

        @DisplayName("게시글 삭제 실패 - POST_NOT_FOUND")
        @Test
        public void deletePost_shouldReturnPostNotFoundError() {
            given(postRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(OurDressingTableException.class, () -> postService.deletePost(1L));

        }
    }

    @Nested
    @DisplayName("게시글 ENTITY 조회 테스트")
    class getPost {
        @DisplayName("게시글 ENTITY 조회 - 성공")
        @Test
        public void getPost_shouldReturnSuccess() {
            Member member = TestDataFactory.testMember(1L);
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(10L);
            Post post = TestDataFactory.testPost(1L,member, communityCategory);

            given(memberService.getMemberEntityById(member.getId())).willReturn(member);
            given(postRepository.findById(1L)).willReturn(Optional.of(post));

            Post result = postService.getValidPostEntityById(1L);

            assertEquals(post, result);

        }
        @DisplayName("게시글 ENTITY 조회 실패 - 삭제된 게시글")
        @Test
        public void getPost_shouldReturnPostNotFoundError() {
             Member member = TestDataFactory.testMember(1L);
             CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(10L);
             Post post = TestDataFactory.testPost(1L, member, communityCategory);
             post.markAsDeleted();

             given(postRepository.findById(1L)).willReturn(Optional.of(post));

             assertThatThrownBy(() ->postService.getValidPostEntityById(1L)).isInstanceOf(OurDressingTableException.class)
                     .hasMessageContaining(ErrorCode.POST_NOT_FOUND.getMessage());
        }
    }

}
