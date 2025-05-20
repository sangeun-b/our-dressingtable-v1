package com.ourdressingtable.community.service;

import com.ourdressingtable.community.domain.CommunityCategory;
import com.ourdressingtable.community.domain.Post;
import com.ourdressingtable.community.dto.CreatePostRequest;
import com.ourdressingtable.community.dto.UpdatePostRequest;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Long createPost(CreatePostRequest request, CommunityCategory category);
    Optional<Post> getPost(Long id);
    List<Post> getPosts();
    void deletePost(Long id);
    void updatePost(UpdatePostRequest request);
    int countPosts();
    int countPostsByCategory(String category);
    List<Post> getPostsByCategory(String category);
    List<Post> getPostsByMember(Long memberId);
    List<Post> getPostsByTitle(String title);
    List<Post> getPostsByContent(String content);

}
