package com.ourdressingtable.community.post.controller;

import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.CreatePostResponse;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.community.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "게시글", description = "게시글 관련 API")
public class PostController {

    private final CommunityService communityService;
    private final PostService postService;

    @PostMapping()
    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        // TODO: 로그인 회원 정보로 변경 필요
        Long memberId = 1L;
        Long id = communityService.createPost(request, memberId);
        return ResponseEntity.created(URI.create("/api/posts/"+id))
                .body(new CreatePostResponse(id));
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePostRequest request) {
        Long memberId = 1L;
        communityService.updatePost(postId, memberId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 단건 조회", description = "게시글을 단건 조회합니다.")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.getPost(postId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public ResponseEntity deletePost(@PathVariable Long postId) {
        // TODO: 로그인 MEMBER 정보
        Long memberId = 1L;
        communityService.deletePost(postId, memberId);
        return ResponseEntity.noContent().build();

    }
}
