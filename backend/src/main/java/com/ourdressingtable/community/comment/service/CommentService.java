package com.ourdressingtable.community.comment.service;

import com.ourdressingtable.community.comment.dto.CreateCommentRequest;

public interface CommentService {
    Long createComment(CreateCommentRequest createCommentRequest, Long memberId);

}
