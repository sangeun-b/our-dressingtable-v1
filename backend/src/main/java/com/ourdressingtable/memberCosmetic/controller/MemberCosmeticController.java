package com.ourdressingtable.membercosmetic.controller;

import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticResponse;
import com.ourdressingtable.membercosmetic.dto.MemberCosmeticResponse;
import com.ourdressingtable.membercosmetic.service.MemberCosmeticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member-cosmetics")
@Tag(name = "화장품", description = "화장품 관련 API")

public class MemberCosmeticController {

    private final MemberCosmeticService memberCosmeticService;

    @PostMapping()
    @Operation(summary = "회원의 화장품 등록", description = "회원이 화장대에 새로운 화장품을 등록합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CreateMemberCosmeticResponse> createMemberCosmetic(@RequestBody @Valid CreateMemberCosmeticRequest request) {
        Long id = memberCosmeticService.createMemberCosmetic(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(location).
                body(new CreateMemberCosmeticResponse(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 화장품 상세 조회", description = "회원의 화장품 상세 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MemberCosmeticResponse> getMemberCosmeticDetail(@PathVariable Long id) {
        return ResponseEntity.ok(memberCosmeticService.getMemberCosmeticDetail(id));
    }

}
