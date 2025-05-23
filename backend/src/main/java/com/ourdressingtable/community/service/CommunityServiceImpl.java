package com.ourdressingtable.community.service;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.exception.ErrorCode;
import com.ourdressingtable.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityServiceImpl implements CommunityService {

    private final PostService postService;
    private final CommunityCategoryService communityCategoryService;
    private final MemberService memberService;

    @Override
    public Long createPost(CreatePostRequest createPostRequest, Long memberId) {
//        Member member = memberService.getMember(memberId);
        return postService.createPost(createPostRequest, memberId);
    }

    @Override
    public void updatePost(Long postId, Long memberId, UpdatePostRequest updatePostRequest) {
        checkPermission(postId, memberId);
        postService.updatePost(postId, updatePostRequest);
    }

    @Override
    public void deletePost(Long postId, Long memberId) {
        checkPermission(postId, memberId);
        postService.deletePost(postId);
    }

    private void checkPermission(Long postId, Long memberId) {
        Post post = postService.getPostEntityById(postId);
        if(!post.getMember().getId().equals(memberId)){
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_TO_EDIT);
        }
    }
}
