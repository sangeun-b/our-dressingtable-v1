package com.ourdressingtable.community.post.controller;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.CreatePostResponse;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "게시글", description = "게시글 관련 API")
public class PostController {

    private final CommunityService communityService;

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @PostMapping()
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = communityService.createPost(request, customUserDetails.getMemberId());
        return ResponseEntity.created(URI.create("/api/posts/"+id))
                .body(CreatePostResponse.builder().id(id).build());
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePostRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        communityService.updatePost(postId, customUserDetails.getMemberId(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글을 단건 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId = customUserDetails != null ? customUserDetails.getMemberId() : null;
        return ResponseEntity.ok(communityService.getPostDetail(postId, memberId));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        communityService.deletePost(postId, customUserDetails.getMemberId());
        return ResponseEntity.noContent().build();

    }
}
