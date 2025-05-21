package com.ourdressingtable.community.service.impl;

import com.ourdressingtable.community.post.dto.CreatePostRequest;
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

}
