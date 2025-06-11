package com.ourdressingtable.community.service;

import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;

public interface CommunityService {

    Long createPost(CreatePostRequest createPostRequest);
    void updatePost(Long postId, UpdatePostRequest updatePostRequest);
    void deletePost(Long postId);
    PostDetailResponse getPostDetail(Long postId);
}
