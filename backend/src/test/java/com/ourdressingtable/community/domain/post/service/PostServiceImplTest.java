package com.ourdressingtable.community.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

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
            Long memberId = 1L;
            CreatePostRequest createPostRequest = CreatePostRequest.builder()
                    .title("제목")
                    .content("내용")
                    .build();
            CommunityCategory communityCategory = CommunityCategory.builder()
                    .id(10L)
                    .name("자유")
                    .build();

            given(communityCategoryService.getCategoryEntityById(10L)).willReturn(communityCategory);
            given(postRepository.save(any(Post.class))).willAnswer(invocation -> {
                Post saved = invocation.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", 100L);
                return saved;
            });

            // when
            Long postId = postService.createPost(createPostRequest, memberId);

            // then
            assertEquals(100L, postId);
            then(postRepository).should().save(any(Post.class));

        }

        @DisplayName("게시글 작성 실패 - MEMBER NOT FOUND")
        @Test
        public void createPost_shouldReturnUserNotFoundError() {
            // given
            Long memberId = 999L;
            CreatePostRequest createPostRequest = CreatePostRequest.builder()
                    .title("제목")
                    .content("내용")
                    .communityCategoryId(1L)
                    .build();

            given(memberService.getMember(memberId)).willThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
        }
    }
    @Nested
    @DisplayName("게시글 수정 테스트")
    class updatePost {
        @DisplayName("게시글 수정 성공")
        @Test
        public void updatePost_shouldReturnSuccess() {
            Long postId = 2L;
            UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                    .title("새로운")
                    .content("업데이트")
                    .communityCategoryId(20L)
                    .build();

            Post post = Post.builder()
                    .title("기존")
                    .content("내용")
                    .member(Member.builder().id(1L).build())
                    .communityCategory(CommunityCategory.builder().id(10L).name("자유").build())
                    .build();

            CommunityCategory newCategory = CommunityCategory.builder().id(20L).name("기타").build();

            given(communityCategoryService.getCategoryEntityById(20L)).willReturn(newCategory);
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            postService.updatePost(postId, updatePostRequest);

            // then
            assertEquals(20L, post.getCommunityCategory().getId());
            assertEquals("새로운", post.getTitle());
            assertEquals("업데이트", post.getContent());
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
                    .isDeleted(true)
                    .member(Member.builder().id(1L).build())
                    .build();

            given(postRepository.findById(1L)).willReturn(Optional.of(post));

            // when
            postService.deletePost(1L);

            // then
            assertThat(post.isDeleted()).isTrue();
            verify(postRepository).delete(post);
        }

        @DisplayName("게시글 삭제 실패 - POST_NOT_FOUND")
        @Test
        public void deletePost_shouldReturnPostNotFoundError() {
            given(postRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(OurDressingTableException.class, () -> postService.deletePost(1L));

        }
    }

}
