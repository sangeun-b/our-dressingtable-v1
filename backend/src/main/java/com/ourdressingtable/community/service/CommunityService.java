package com.ourdressingtable.community.service;

import com.ourdressingtable.community.post.dto.CreatePostRequest;

public interface CommunityService {

    Long createPost(CreatePostRequest createPostRequest);
}
