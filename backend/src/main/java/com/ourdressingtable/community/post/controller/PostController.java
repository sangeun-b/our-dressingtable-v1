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
        Long id = communityService.createPost(request);
        return ResponseEntity.created(URI.create("/api/posts/"+id))
                .body(new CreatePostResponse(id));
    }

    @PatchMapping("/{postId}/{memberId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId, @PathVariable("memberId") Long memberId, @RequestBody @Valid UpdatePostRequest request) {
        communityService.updatePost(postId, memberId, request);
        return ResponseEntity.noContent().build();
    }
}
