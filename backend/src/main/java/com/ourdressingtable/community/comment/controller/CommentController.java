package com.ourdressingtable.community.comment.controller;

import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.CreateCommentResponse;
import com.ourdressingtable.community.comment.repository.CommentRepository;
import com.ourdressingtable.community.comment.service.CommentService;
import com.ourdressingtable.community.post.dto.CreatePostResponse;
import com.ourdressingtable.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "댓글", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "새 댓글을 작성합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping()
    public ResponseEntity<CreateCommentResponse> createComment(@RequestBody @Valid CreateCommentRequest request) {
        Long commentId = commentService.createComment(request);
        return ResponseEntity.created(URI.create("/api/comments/" + commentId)).body(
                CreateCommentResponse.builder().id(commentId).build());
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


}
