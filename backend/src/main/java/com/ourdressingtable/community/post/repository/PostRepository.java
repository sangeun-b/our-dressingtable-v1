package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{
    List<Post> member(Member member);
}
