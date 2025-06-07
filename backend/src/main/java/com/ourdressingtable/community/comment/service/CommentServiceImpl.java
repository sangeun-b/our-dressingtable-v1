package com.ourdressingtable.community.comment.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.repository.CommentRepository;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    @Override
    @Transactional
    public Long createComment(CreateCommentRequest request, Long memberId) {
        Member member = memberService.getActiveMemberEntityById(memberId);
        Post post = postService.getValidPostEntityById(request.getPostId());

        Comment parent = null;
        int depth = 0;
        if(request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId()).orElseThrow(() -> new OurDressingTableException(
                    ErrorCode.COMMENT_NOT_FOUND));
            depth = parent.getDepth() + 1;
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .depth(depth)
                .member(member)
                .post(post)
                .parent(parent)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }

}
