package com.ourdressingtable.member.controller;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.CreateMemberResponse;
import com.ourdressingtable.member.dto.MemberResponse;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .nickname(request.getNickname())
                .phoneNumber(request.getPhoneNumber())
                .skinType(request.getSkinType())
                .colorType(request.getColorType())
                .birthDate(request.getBirthDate())
                .imageUrl(request.getImageUrl())
                .build();

        Long id = memberService.createMember(createMemberRequest);
        return ResponseEntity.created(URI.create("/api/members/" + id))
                .body(new CreateMemberResponse(id));
    }

    // 다른 회원 프로필 조회
    @GetMapping("/{memberId}")
    @Operation(summary = "다른 회원 조회", description = "다른 회원의 프로필을 조회합니다.")
    public ResponseEntity<OtherMemberResponse> getOtherMember(@PathVariable("memberId") Long memberId) {
        OtherMemberResponse otherMemberResponse = memberService.getOtherMember(memberId);
        return ResponseEntity.ok(otherMemberResponse);

    }

    @PatchMapping("/{memberId}")
    @Operation(summary = "회원 수정", description = "회원 정보를 수정합니다.", security = @SecurityRequirement(name="bearerAuth"))
    public ResponseEntity updateMember(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateMemberRequest updateMemberRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(!memberId.equals(customUserDetails.getMemberId())) {
            throw new OurDressingTableException(ErrorCode.FORBIDDEN);
        }
        memberService.updateMember(memberId, updateMemberRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "회원 삭제", description = "회원을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity deleteMember(@PathVariable("memberId") Long memberId, @RequestBody @Valid WithdrawalMemberRequest withdrawalMemberRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(!memberId.equals(customUserDetails.getMemberId())) {
            throw new OurDressingTableException(ErrorCode.FORBIDDEN);
        }
        memberService.withdrawMember(memberId, withdrawalMemberRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-information")
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    public ResponseEntity<?> getCurrentMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = memberService.getActiveMemberEntityById(userDetails.getMemberId());
        MemberResponse response = MemberResponse.builder()
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .phoneNumber(member.getPhoneNumber())
                .skinType(member.getSkinType())
                .colorType(member.getColorType())
                .birthDate(member.getBirthDate())
                .role(member.getRole().getAuth())
                .build();
        return ResponseEntity.ok(response);
    }
}
