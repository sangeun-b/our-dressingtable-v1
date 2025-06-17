package com.ourdressingtable.community.post.service;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Long createPost(CreatePostRequest request, Long memberId);
    Page<PostResponse> getPosts(PostSearchCondition condition, Pageable pageable);
    void deletePost(Long postId);
    void updatePost(Long postId, UpdatePostRequest request);
    int countPosts();
    int countPostsByCategory(String category);
    Post getPostEntityById(Long id);
    Post getValidPostEntityById(Long id);
    Page<PostResponse> getMyPosts(Long memberId, Pageable pageable, MyPostSearchCondition condition);
    Page<PostResponse> getLikedPosts(Long memberId, Pageable pageable, MyPostSearchCondition condition);
    Page<PostResponse> getCommentedPosts(Long memberId, Pageable pageable, MyPostSearchCondition condition);
}
