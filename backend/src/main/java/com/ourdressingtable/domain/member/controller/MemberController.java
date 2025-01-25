package com.ourdressingtable.domain.member.controller;

import com.ourdressingtable.domain.member.dto.CreateMemberRequest;
import com.ourdressingtable.domain.member.dto.CreateMemberResponse;
import com.ourdressingtable.domain.member.service.MemberService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
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
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        Long id = memberService.createMember(createMemberRequest);
        return ResponseEntity.created(URI.create("/api/member/" + id))
                .body(new CreateMemberResponse(id));
    }
}
