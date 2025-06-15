package com.ourdressingtable.community.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.UpdateCommentRequest;
import com.ourdressingtable.community.comment.repository.CommentRepository;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.service.PostService;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("댓글 SERVICE 테스트")
public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    @Mock
    private MemberService memberService;

    @Nested
    @DisplayName("댓글 작성 테스트")
    class createComment {

        @DisplayName("댓글 작성 성공 - 단일 댓글")
        @Test
        public void createComment_returnSuccess() {
            Member member = TestDataFactory.testMember(1L);
            Post post = TestDataFactory.testPost(1L, member, TestDataFactory.testCommunityCategory(1L));

            CreateCommentRequest request = TestDataFactory.testCreateCommentRequest(post.getId());

            given(memberService.getActiveMemberEntityById(anyLong())).willReturn(member);
            given(postService.getValidPostEntityById(anyLong())).willReturn(post);
            given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> {
                Comment saved = invocation.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", 1L);
                return saved;
            });

            Long createdId = commentService.createComment(request, member);

            assertNotNull(createdId);
            assertEquals(1L, createdId);

        }

        @DisplayName("댓글 작성 성공 -대댓글")
        @Test
        public void createComment_withParent_returnSuccess() {
            Member member = TestDataFactory.testMember(1L);
            Post post = TestDataFactory.testPost(1L, member, TestDataFactory.testCommunityCategory(1L));
            Comment parentComment = TestDataFactory.testComment(2L);

            CreateCommentRequest request = TestDataFactory.testCreateCommentRequestWithParent(post.getId(),parentComment.getId());

            given(memberService.getActiveMemberEntityById(anyLong())).willReturn(member);
            given(postService.getValidPostEntityById(anyLong())).willReturn(post);
            given(commentRepository.findById(eq(parentComment.getId()))).willReturn(Optional.of(parentComment));
            given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> {
                Comment saved = invocation.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", 3L);
                return saved;
            });

            Long createdId = commentService.createComment(request, member);

            assertNotNull(createdId);
            assertEquals(3L, createdId);

        }

        @DisplayName("댓글 작성 실패 - 부모 댓글이 존재하지 않음")
        @Test
        public void createComment_withParent_returnCommentNotFoundError() {
            Member member = TestDataFactory.testMember(1L);
            Post post = TestDataFactory.testPost(1L, member, TestDataFactory.testCommunityCategory(1L));

            CreateCommentRequest request = TestDataFactory.testCreateCommentRequestWithParent(
                    post.getId(), 999L);

            given(memberService.getActiveMemberEntityById(anyLong())).willReturn(member);
            given(postService.getValidPostEntityById(anyLong())).willReturn(post);
            given(commentRepository.findById(eq(999L))).willReturn(Optional.empty());

            assertThrows(OurDressingTableException.class, () -> {
                commentService.createComment(request, member);
            });
        }

    }

    @Nested
    @DisplayName("댓글 수정 테스트")
    class updateComment {
        @DisplayName("댓글 수정 성공")
        @Test
        public void updateComment_returnSuccess() {
            Comment comment = TestDataFactory.testComment(1L);
            given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

            UpdateCommentRequest updateCommentRequest = TestDataFactory.testUpdateCommentRequest(1L);

            commentService.updateComment(1L, updateCommentRequest);

            assertEquals("수정된 내용", comment.getContent());

        }
        
        @DisplayName("댓글 수정 삭제 - 존재하지 않는 댓글")
        @Test
        public void updateComment_returnNotFoundError() {
            given(commentRepository.findById(1L)).willReturn(Optional.empty());
            UpdateCommentRequest updateCommentRequest = TestDataFactory.testUpdateCommentRequest(1L);

            assertThrows(OurDressingTableException.class, () -> commentService.updateComment(1L, updateCommentRequest));
        }

    }

    @Nested
    @DisplayName("댓글 삭제 테스트")
    class deleteComment {

        @DisplayName("댓글 삭제 성공")
        @Test
        public void deleteComment_returnSuccess() {
            Comment comment = TestDataFactory.testComment(1L);
            // given
            given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

            commentService.deleteComment(comment.getId());

            assertTrue(comment.isDeleted());

        }

        @DisplayName("댓글 삭제 실패 - 존재하지 않는 댓글")
        @Test
        public void deleteComment_returnNotFoundError() {
            given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThrows(OurDressingTableException.class, () -> {
                commentService.deleteComment(999L);
            });
        }
    }

}
