package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.community.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
