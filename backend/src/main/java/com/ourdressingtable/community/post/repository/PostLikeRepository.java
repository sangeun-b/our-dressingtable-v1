package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.domain.PostLike;
import com.ourdressingtable.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMemberAndPost(Member member, Post post);
    boolean existsByMemberAndPost(Member member, Post post);
}
