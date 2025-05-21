package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.community.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
