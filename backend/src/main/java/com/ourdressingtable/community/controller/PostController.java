package com.ourdressingtable.community.controller;

import com.ourdressingtable.community.dto.CreatePostRequest;
import com.ourdressingtable.community.dto.CreatePostResponse;
import com.ourdressingtable.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        Long id = postService.createPost(request);
        return ResponseEntity.created(URI.create("/api/communities/"+id))
         .body(new CreatePostResponse(id));
    }
}
