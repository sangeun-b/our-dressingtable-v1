package com.ourdressingtable.community.controller;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.CreateCommentResponse;
import com.ourdressingtable.community.comment.service.CommentService;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.CreatePostResponse;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.PostLikeResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.community.service.CommunityService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
@Tag(name = "커뮤니티", description = "커뮤니티 상호작용 API (상세조회, 좋아요 등)")
public class CommunityController {

    private final CommunityService communityService;
    private final CommentService commentService;

    @Operation(summary = "게시글 상세 조회", description = "로그인 여부에 따라 좋아요 여부를 포함한 게시글 상세정보를 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(communityService.getPostDetail(postId));
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @PostMapping("/posts")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        Long id = communityService.createPost(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location)
                .body(CreatePostResponse.builder().id(id).build());
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PatchMapping("/posts/{postId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePostRequest request) {
        communityService.updatePost(postId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId) {
        communityService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "좋아요 등록", description = "게시글에 좋아요를 등록합니다.")
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<PostLikeResponse> postLike(@PathVariable Long postId) {
        boolean liked = communityService.toggleLike(postId);
        return ResponseEntity.ok(PostLikeResponse.builder().liked(liked).build());
    }

    @Operation(summary = "댓글 작성", description = "새 댓글을 작성합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(@RequestBody @Valid CreateCommentRequest request) {
        Long commentId = commentService.createComment(request);
        return ResponseEntity.created(URI.create("/api/comments/" + commentId)).body(
                CreateCommentResponse.builder().id(commentId).build());
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
