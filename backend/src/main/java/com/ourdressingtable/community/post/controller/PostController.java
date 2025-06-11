package com.ourdressingtable.community.post.controller;

import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.CreatePostResponse;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.PostResponse;
import com.ourdressingtable.community.post.dto.PostSearchCondition;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Operation(summary = "게시글 목록 조회", description = "조건에 따라 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(
            @ModelAttribute PostSearchCondition condition,
            Pageable pageable) {
        return ResponseEntity.ok(postService.getPosts(condition,pageable));
    }

}
