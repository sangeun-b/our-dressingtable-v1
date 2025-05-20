package com.ourdressingtable.community.service;

import com.ourdressingtable.community.dto.CreatePostRequest;

public interface CommunityService {

    Long createPost(CreatePostRequest createPostRequest);
}
