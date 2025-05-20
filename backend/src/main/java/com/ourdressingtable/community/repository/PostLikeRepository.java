package com.ourdressingtable.community.repository;

import com.ourdressingtable.community.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
