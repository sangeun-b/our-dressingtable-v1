package com.ourdressingtable.community.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.domain.PostLike;
import com.ourdressingtable.community.post.repository.PostLikeRepository;
import com.ourdressingtable.community.post.service.PostLikeServiceImpl;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@DisplayName("게시글 좋아요 기능 테스트")
public class PostLikeServiceImplTest {

    @InjectMocks
    private PostLikeServiceImpl postLikeService;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private PostService postService;

    @Nested
    @DisplayName("게시글 좋아요 테스트")
    class togglePostLike {
        @DisplayName("게시글 좋아요 추가 성공 테스트")
        @Test
        public void togglePostLike_add_returnSuccess() {
            Member member = TestDataFactory.testMember(1L);
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(1L);
            Post post = TestDataFactory.testPost(1L, member, communityCategory);
            int beforeLikeCount = post.getLikeCount();

            given(memberService.getActiveMemberEntityById(member.getId())).willReturn(member);
            given(postService.getPostEntityById(post.getId())).willReturn(post);
            given(postLikeRepository.findByMemberAndPost(member, post)).willReturn(Optional.empty());

            boolean isLiked = postLikeService.toggleLike(post.getId(), member.getId());

            assertTrue(isLiked);
            assertEquals(beforeLikeCount+1, post.getLikeCount());
            verify(postLikeRepository).save(any(PostLike.class));
        }

        @DisplayName("게시글 좋아요 취소 성공 테스트")
        @Test
        public void togglePostLike_remove_returnSuccess() {
            Member member = TestDataFactory.testMember(1L);
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(1L);
            Post post = TestDataFactory.testPost(1L, member, communityCategory);
            PostLike postLike = TestDataFactory.testPostLike(1L, member, post);
            int beforeLikeCount = post.getLikeCount();

            given(memberService.getActiveMemberEntityById(member.getId())).willReturn(member);
            given(postService.getPostEntityById(post.getId())).willReturn(post);
            given(postLikeRepository.findByMemberAndPost(member, post)).willReturn(Optional.of(postLike));

            boolean isLiked = postLikeService.toggleLike(post.getId(), member.getId());

            assertFalse(isLiked);
            assertEquals(beforeLikeCount-1, post.getLikeCount());
            verify(postLikeRepository).delete(postLike);
        }

        @DisplayName("게시글 좋아요 실패 - USER NOT FOUND")
        @Test
        public void togglePostLike_returnMemberNotFoundError() {
            Long memberId = 99L;
            Long postId = 1L;

            given(memberService.getActiveMemberEntityById(memberId)).willThrow(new OurDressingTableException(
                    ErrorCode.MEMBER_NOT_FOUND));

            assertThrows(OurDressingTableException.class, () -> {
                postLikeService.toggleLike(postId, memberId);
            });
        }
    }

    @Nested
    @DisplayName("좋아요 상태 확인 테스트")
    class HasLikedTest {

        @DisplayName("좋아요 상태 확인 성공")
        @Test
        public void hasLiked_returnSuccess() {
            Member member = TestDataFactory.testMember(1L);
            CommunityCategory communityCategory = TestDataFactory.testCommunityCategory(1L);
            Post post = TestDataFactory.testPost(1L, member, communityCategory);

            given(memberService.getMemberEntityById(member.getId())).willReturn(member);
            given(postService.getPostEntityById(post.getId())).willReturn(post);
            given(postLikeRepository.existsByMemberAndPost(member, post)).willReturn(true);

            boolean isLiked = postLikeService.hasLiked(post.getId(), member.getId());

            assertTrue(isLiked);
        }
    }
}
