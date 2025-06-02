package com.ourdressingtable.community.post.service;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;

import java.util.List;

public interface PostService {
    Long createPost(CreatePostRequest request, Long memberId);
    List<Post> getPosts();
    void deletePost(Long postI);
    void updatePost(Long postId, UpdatePostRequest request);
    int countPosts();
    int countPostsByCategory(String category);
    List<Post> getPostsByCategory(String category);
    List<Post> getPostsByMember(Long memberId);
    List<Post> getPostsByTitle(String title);
    List<Post> getPostsByContent(String content);
    Post getPostEntityById(Long id);
    Post getValidPostEntityById(Long id);
}
