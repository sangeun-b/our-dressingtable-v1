package com.ourdressingtable.community.service.impl;

import com.ourdressingtable.community.domain.CommunityCategory;
import com.ourdressingtable.community.domain.Post;
import com.ourdressingtable.community.dto.CommunityCategoryResponse;
import com.ourdressingtable.community.dto.CreatePostRequest;
import com.ourdressingtable.community.service.CommunityCategoryService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.community.service.PostService;
import com.ourdressingtable.member.domain.Member;
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
        CommunityCategory communityCategory = communityCategoryService.getCategoryById(createPostRequest.getCommunityCategoryId());
        Member member = new Member();
        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .communityCategory(communityCategory)
                .member(member)
                .build();
        return postService.createPost(createPostRequest, communityCategory);
    }
}
