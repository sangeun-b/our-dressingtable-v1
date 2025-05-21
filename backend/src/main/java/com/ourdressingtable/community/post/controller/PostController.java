package com.ourdressingtable.community.post.controller;

import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.CreatePostResponse;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.service.CommunityService;
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
public class PostController {

    private final CommunityService communityService;

    @PostMapping()
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        // TODO: 로그인 회원 정보로 변경 필요
        Long memberId = 1L;
        Long id = communityService.createPost(request, memberId);
        return ResponseEntity.created(URI.create("/api/posts/"+id))
                .body(new CreatePostResponse(id));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePostRequest request) {
        Long memberId = 1L;
        communityService.updatePost(postId, memberId, request);
        return ResponseEntity.noContent().build();
    }
}
