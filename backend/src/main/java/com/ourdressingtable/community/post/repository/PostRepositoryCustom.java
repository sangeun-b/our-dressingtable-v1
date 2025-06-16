package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.channels.FileChannel;

public interface PostRepositoryCustom {

    Page<Post> search(PostSearchCondition condition, Pageable pageable);
    Page<Post> findMyPosts(Long memberId, Pageable pageable, String sortBy);
    Page<Post> findLikedPosts(Long memberId, Pageable pageable, String sortBy);
    Page<Post> findCommentedPosts(Long memberId, Pageable pageable, String sortBy);
}
