package com.ourdressingtable.member.controller;

import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.CreateMemberResponse;
import com.ourdressingtable.member.dto.MemberResponse;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberResponse;
import com.ourdressingtable.member.service.MemberService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
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
//    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
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
                .role(request.getRole())
                .build();

        Long id = memberService.createMember(createMemberRequest);
        return ResponseEntity.created(URI.create("/api/members/" + id))
                .body(new CreateMemberResponse(id));
    }

    // 다른 회원 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<OtherMemberResponse> getMember(@PathVariable("userId") Long userId) {
        OtherMemberResponse otherMemberResponse = memberService.getMember(userId);
        return ResponseEntity.ok(otherMemberResponse);

    }

    @PatchMapping("/{userId}")
    public ResponseEntity updateMember(@PathVariable("userId") Long userId, @RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(userId, updateMemberRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteMember(@PathVariable("userId") Long userId, @RequestBody @Valid WithdrawalMemberRequest withdrawalMemberRequest) {
        memberService.deleteMember(userId, withdrawalMemberRequest);
        return ResponseEntity.noContent().build();
    }
}
