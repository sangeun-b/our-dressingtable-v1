package com.ourdressingtable.community.post.controller;

import com.ourdressingtable.community.post.dto.PostLikeResponse;
import com.ourdressingtable.community.post.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "좋아요", description = "게시글 좋아요 관련 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(summary = "좋아요 등록", description = "게시글에 좋아요를 등록합니다.")
    @PostMapping("/{postId}/like")
    public ResponseEntity<PostLikeResponse> postLike(@PathVariable Long postId, @RequestParam Long memberId) {
        // TODO: 로그인 한 회원 정보
        boolean liked = postLikeService.postLike(memberId, postId);
        return ResponseEntity.ok(PostLikeResponse.builder().liked(liked).build());
    }
}
