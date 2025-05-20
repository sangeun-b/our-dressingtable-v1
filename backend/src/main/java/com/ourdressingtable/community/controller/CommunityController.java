package com.ourdressingtable.community.controller;

import com.ourdressingtable.community.dto.CreatePostRequest;
import com.ourdressingtable.community.dto.CreatePostResponse;
import com.ourdressingtable.community.service.CommunityService;
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
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/posts")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        Long id = communityService.createPost(request);
        return ResponseEntity.created(URI.create("/api/communities/"+id))
                .body(new CreatePostResponse(id));
    }
}
