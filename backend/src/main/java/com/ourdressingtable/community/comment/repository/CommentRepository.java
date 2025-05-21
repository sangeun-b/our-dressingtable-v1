package com.ourdressingtable.community.comment.repository;

import com.ourdressingtable.community.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
