package com.ourdressingtable.community.comment.controller;

import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.CreateCommentResponse;
import com.ourdressingtable.community.comment.repository.CommentRepository;
import com.ourdressingtable.community.comment.service.CommentService;
import com.ourdressingtable.community.post.dto.CreatePostResponse;
import com.ourdressingtable.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "댓글", description = "댓글 관련 API")
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "새 댓글을 작성합니다.")
    @PostMapping()
    public ResponseEntity<CreatePostResponse> createComment(@RequestBody @Valid CreateCommentRequest request, @AuthenticationPrincipal
            CustomUserDetails customUserDetails) {
        Long commentId = commentService.createComment(request, customUserDetails.getMemberId());
        return ResponseEntity.created(URI.create("/api/comments/" + commentId)).body(
                CreateCommentResponse.builder().id(commentId).build());
    }

}
