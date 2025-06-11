package com.ourdressingtable.community.comment.service;

import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.UpdateCommentRequest;
import com.ourdressingtable.member.domain.Member;

public interface CommentService {
    Long createComment(CreateCommentRequest createCommentRequest, Member member);
    void deleteComment(Long id);
    Comment getValidCommentEntityById(Long id);
    void updateComment(Long id, UpdateCommentRequest updateCommentRequest);
}
