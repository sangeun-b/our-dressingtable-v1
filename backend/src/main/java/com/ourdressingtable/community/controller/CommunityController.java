package com.ourdressingtable.community.controller;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.CreateCommentResponse;
import com.ourdressingtable.community.comment.dto.UpdateCommentRequest;
import com.ourdressingtable.community.comment.service.CommentService;
import com.ourdressingtable.community.post.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
@Tag(name = "커뮤니티", description = "커뮤니티 상호작용 API (상세조회, 좋아요 등)")
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "사용자가 작성한 게시글 조회", description = "사용자가 작성한 게시글 목록을 조회합니다.")
    @GetMapping("/posts/me")
    public ResponseEntity<Page<PostResponse>> getMyPosts(@RequestParam(defaultValue = "createdAt") String sortBy, Pageable pageable) {
        return ResponseEntity.ok(communityService.getMyPosts(pageable, sortBy));
    }

    @Operation(summary = "사용자가 좋아요한 게시글 조회", description = "사용자가 좋아요한 게시글 목록을 조회합니다.")
    @GetMapping("/posts/me/likes")
    public ResponseEntity<Page<PostResponse>> getLikedPost(@RequestParam(defaultValue = "createdAt") String sortBy, Pageable pageable) {
        return ResponseEntity.ok(communityService.getLikedPosts(pageable, sortBy));
    }

    @Operation(summary = "사용자가 댓글 단 게시글 조회", description = "사용자가 댓글을 작성한 게시글 목록을 조회합니다.")
    @GetMapping("/posts/me/comments")
    public ResponseEntity<Page<PostResponse>> getCommentedPost(@RequestParam(defaultValue = "createdAt") String sortBy, Pageable pageable) {
        return ResponseEntity.ok(communityService.getCommentedPosts(pageable, sortBy));
    }

    @Operation(summary = "게시글 상세 조회", description = "로그인 여부에 따라 좋아요 여부를 포함한 게시글 상세정보를 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(communityService.getPostDetail(postId));
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/posts")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        Long id = communityService.createPost(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location)
                .body(CreatePostResponse.builder().id(id).build());
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/posts/{postId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePostRequest request) {
        communityService.updatePost(postId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId) {
        communityService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "좋아요 등록", description = "게시글에 좋아요를 등록합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<PostLikeResponse> postLike(@PathVariable Long postId) {
        boolean liked = communityService.toggleLike(postId);
        return ResponseEntity.ok(PostLikeResponse.builder().liked(liked).build());
    }

    @Operation(summary = "댓글 작성", description = "새 댓글을 작성합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(@RequestBody @Valid CreateCommentRequest request) {
        Long commentId = communityService.createComment(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(commentId).toUri();
        return ResponseEntity.created(location).body(
                CreateCommentResponse.builder().id(commentId).build());
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
        communityService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") Long commentId, @RequestBody @Valid UpdateCommentRequest updateCommentRequest) {
        communityService.updateComment(commentId, updateCommentRequest);
        return ResponseEntity.noContent().build();
    }
}
