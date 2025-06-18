package com.ourdressingtable.community.service;

import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.UpdateCommentRequest;
import com.ourdressingtable.community.comment.service.CommentService;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.*;
import com.ourdressingtable.community.post.service.PostLikeService;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableArgumentResolver;
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
    private final PageableArgumentResolver pageableArgumentResolver;

    @Override
    @Transactional
    public Long createPost(CreatePostRequest createPostRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
//        Member member = memberService.getActiveMemberEntityById(memberId);
        return postService.createPost(createPostRequest, memberId);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostRequest updatePostRequest) {
        validatePostOwner(postId);
        postService.updatePost(postId, updatePostRequest);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        validatePostOwner(postId);
        postService.deletePost(postId);
    }

    private void validatePostOwner(Long postId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        memberService.getActiveMemberEntityById(memberId);
        Post post = postService.getValidPostEntityById(postId);
        if(!post.getMember().getId().equals(memberId)){
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT);
        }
    }

    private void validateCommentOwner(Long commentId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        memberService.getActiveMemberEntityById(memberId);
        Comment comment = commentService.getValidCommentEntityById(commentId);
        if(!comment.getMember().getId().equals(memberId)){
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT);
        }
    }



    @Override
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postService.getValidPostEntityById(postId);
        Long memberId = SecurityUtil.getCurrentMemberId();
//        Member member = memberService.getActiveMemberEntityById(memberId);
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
        return commentService.createComment(createCommentRequest, member);
    }

    @Override
    public void deleteComment(Long commentId) {
        validateCommentOwner(commentId);
        commentService.deleteComment(commentId);

    }

    @Override
    public void updateComment(Long commentId, UpdateCommentRequest updateCommentRequest) {
        validateCommentOwner(commentId);
        commentService.updateComment(commentId, updateCommentRequest);

    }

    @Override
    public Page<PostResponse> getMyPosts(Pageable pageable, MyPostSearchCondition condition) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return postService.getMyPosts(memberId, pageable, condition);
    }

    @Override
    public Page<PostResponse> getLikedPosts(Pageable pageable, MyPostSearchCondition condition) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return postService.getLikedPosts(memberId, pageable, condition);
    }

    @Override
    public Page<PostResponse> getCommentedPosts(Pageable pageable, MyPostSearchCondition condition) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return postService.getCommentedPosts(memberId, pageable, condition);
    }
}
