package com.ourdressingtable.community.service;

import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.UpdateCommentRequest;
import com.ourdressingtable.community.post.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityService {

    Long createPost(CreatePostRequest createPostRequest);
    void updatePost(Long postId, UpdatePostRequest updatePostRequest);
    void deletePost(Long postId);
    PostDetailResponse getPostDetail(Long postId);
    boolean toggleLike(Long postId);
    Long createComment(CreateCommentRequest createCommentRequest);
    void updateComment(Long commentId, UpdateCommentRequest updateCommentRequest);
    void deleteComment(Long commentId);
    Page<PostResponse> getMyPosts(Pageable pageable, MyPostSearchCondition condition);
    Page<PostResponse> getLikedPosts(Pageable pageable, MyPostSearchCondition condition);
    Page<PostResponse> getCommentedPosts(Pageable pageable, MyPostSearchCondition condition);

}
