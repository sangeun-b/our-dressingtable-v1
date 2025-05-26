package com.ourdressingtable.community.service;

import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;

public interface CommunityService {

    Long createPost(CreatePostRequest createPostRequest, Long memberId);
    void updatePost(Long postId, Long memberId, UpdatePostRequest updatePostRequest);
    void deletePost(Long postId, Long memberId);
    PostDetailResponse getPostDetail(Long postId, Long memberId);
}
