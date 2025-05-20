package com.ourdressingtable.community.repository;

import com.ourdressingtable.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
