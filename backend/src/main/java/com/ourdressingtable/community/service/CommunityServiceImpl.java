package com.ourdressingtable.community.service;

import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.UpdateCommentRequest;
import com.ourdressingtable.community.comment.service.CommentService;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostLikeService;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityServiceImpl implements CommunityService {

    private final PostService postService;
    private final CommunityCategoryService communityCategoryService;
    private final MemberService memberService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    @Override
    @Transactional
    public Long createPost(CreatePostRequest createPostRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        if(member == null) {
            throw new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return postService.createPost(createPostRequest, memberId);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostRequest updatePostRequest) {
        checkPostPermission(postId);
        postService.updatePost(postId, updatePostRequest);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        checkPostPermission(postId);
        postService.deletePost(postId);
    }

    private void checkPostPermission(Long postId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        if(member == null) {
            throw new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Post post = postService.getValidPostEntityById(postId);
        if(!post.getMember().getId().equals(memberId)){
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT);
        }
    }

    private void checkCommentPermission(Long commentId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        if(member == null) {
            throw new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Comment comment = commentService.getValidCommentEntityById(commentId);
        if(!comment.getMember().getId().equals(memberId)){
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT);
        }
    }



    @Override
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postService.getValidPostEntityById(postId);
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        boolean liked = false;
        if(memberId != null){
            liked = postLikeService.hasLiked(postId, memberId);
        }
        return PostDetailResponse.from(post, liked);
    }

    @Override
    @Transactional
    public boolean toggleLike(Long postId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return postLikeService.toggleLike(postId, memberId);
    }

    @Override
    public Long createComment(CreateCommentRequest createCommentRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        if(member == null) {
            throw new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return commentService.createComment(createCommentRequest, member);
    }

    @Override
    public void deleteComment(Long commentId) {
        checkCommentPermission(commentId);
        commentService.deleteComment(commentId);

    }

    @Override
    public void updateComment(Long commentId, UpdateCommentRequest updateCommentRequest) {
        checkCommentPermission(commentId);
        commentService.updateComment(commentId, updateCommentRequest);

    }
}
