package com.ourdressingtable.community.service;

import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityServiceImpl implements CommunityService {

    private final PostService postService;
    private final CommunityCategoryService communityCategoryService;

    @Override
    public Long createPost(CreatePostRequest createPostRequest) {
        Long memberId = 1L;
        return postService.createPost(createPostRequest, memberId);
    }

    @Override
    public void updatePost(Long postId, UpdatePostRequest updatePostRequest) {
        // TODO: 포스트 작성자 인지 확인 필요
        postService.updatePost(postId, updatePostRequest);
    }
}
