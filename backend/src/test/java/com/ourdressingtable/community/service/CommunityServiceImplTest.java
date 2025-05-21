package com.ourdressingtable.community.service;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.communityCategory.dto.CommunityCategoryResponse;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommunityServiceImplTest {

    @InjectMocks
    private CommunityServiceImpl communityService;

    @Mock
    private PostService postService;

    @Mock
    private CommunityCategoryService communityCategoryService;

    @Nested
    @DisplayName("게시글 작성 테스트")
    class createPost {
        @DisplayName("게시글 작성 성공")
        @Test
        public void createPosts_shouldReturnSuccess() {
            // given
            CreatePostRequest createPostRequest = CreatePostRequest.builder()
                    .title("제목")
                    .content("내용")
                    .communityCategoryId(1L)
                    .build();
            CommunityCategory category = CommunityCategory.builder().id(1L).name("자유").build();

            given(communityCategoryService.getCategoryById(1L)).willReturn(CommunityCategoryResponse.from(category));
            given(postService.createPost(createPostRequest,1L)).willReturn(123L);

            // when
            Long postId = communityService.createPost(createPostRequest);

            // then
            assertEquals(123L, postId);
            then(postService).should().createPost(createPostRequest,1L);
        }

        // TODO: 게시글 작성 실패 테스트
        @DisplayName("게시글 작성 실패_비회원")
        @Test
        public void createPosts_shouldReturnError() {}

    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class updatePost {
        @DisplayName("게시글 수정 성공")
        @Test
        public void updatePost_shouldReturnSuccess() {
            // given
            Member member = Member.builder().id(1L).name("이름").build();
            Post post = Post.builder().id(1L).title("기존").content("내용").member(member).build();
            UpdatePostRequest updatePostRequest = UpdatePostRequest.builder().title("수정").content("변경").build();

            given(postService.getPostEntityById(1L)).willReturn(post);

            // when
            communityService.updatePost(1L, 1L, updatePostRequest);

            // then
            then(postService).should().updatePost(1L, updatePostRequest);


        }

        @DisplayName("게시글 수정 실패_작성자 미일치")
        @Test
        public void updatePost_shouldReturnError() {
            // given
            Post post = Post.builder().id(1L).title("기존").content("내용").member(Member.builder().id(2L).build()).build();
            UpdatePostRequest updatePostRequest = UpdatePostRequest.builder().title("수정").content("변경").build();

            given(postService.getPostEntityById(1L)).willReturn(post);

            // when & then
            assertThrows(OurDressingTableException.class, () -> {
                communityService.updatePost(1L, 1L, updatePostRequest);
            });
        }
    }

}
