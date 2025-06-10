package com.ourdressingtable.community.controller;

import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
@Tag(name = "커뮤니티", description = "커뮤니티 상호작용 API (상세조회, 좋아요 등)")
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "게시글 단건 조회", description = "게시글을 단건 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(communityService.getPostDetail(postId, memberId));
    }

}
