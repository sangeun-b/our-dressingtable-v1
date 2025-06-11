package com.ourdressingtable.community.service;

import com.ourdressingtable.common.util.SecurityUtil;
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
        checkPermission(postId);
        postService.updatePost(postId, updatePostRequest);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        checkPermission(postId);
        postService.deletePost(postId);
    }

    private void checkPermission(Long postId) {
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
}
