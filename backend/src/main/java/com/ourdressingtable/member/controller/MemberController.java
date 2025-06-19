package com.ourdressingtable.member.controller;

import com.ourdressingtable.member.dto.request.CreateMemberRequest;
import com.ourdressingtable.member.dto.response.CreateMemberResponse;
import com.ourdressingtable.member.dto.response.MemberResponse;
import com.ourdressingtable.member.dto.response.OtherMemberResponse;
import com.ourdressingtable.member.dto.request.UpdateMemberRequest;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 회원이 가입합니다.")
    public ResponseEntity<CreateMemberResponse> signupMember(@RequestBody @Valid
    CreateMemberRequest request) {
        Long id = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/api/members/" + id))
                .body(new CreateMemberResponse(id));
    }

    // 다른 회원 프로필 조회
    @GetMapping("/{memberId}")
    @Operation(summary = "다른 회원 조회", description = "다른 회원의 프로필을 조회합니다.")
    public ResponseEntity<OtherMemberResponse> getOtherMember(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(memberService.getOtherMember(memberId));

    }

    @PatchMapping("/my-information")
    @Operation(summary = "내 정보 수정", description = "내 정보를 수정합니다.", security = @SecurityRequirement(name="bearerAuth"))
    public ResponseEntity<Void> updateMyInformation(@RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(updateMemberRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/my-account")
    @Operation(summary = "내 계정 삭제", description = "내 계정을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteMyAccount(@RequestBody @Valid WithdrawalMemberRequest withdrawalMemberRequest) {
        memberService.withdrawMember(withdrawalMemberRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-information")
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MemberResponse> getMyInformation() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }
}
