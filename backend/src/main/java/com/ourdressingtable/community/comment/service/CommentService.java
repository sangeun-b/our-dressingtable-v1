package com.ourdressingtable.community.comment.service;

import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;

public interface CommentService {
    Long createComment(CreateCommentRequest createCommentRequest);
    void deleteComment(Long commentId);
    Comment getValidCommentEntityById(Long id);

}
