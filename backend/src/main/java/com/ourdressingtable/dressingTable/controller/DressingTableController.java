package com.ourdressingtable.dressingTable.controller;

import com.ourdressingtable.dressingTable.domain.DressingTable;
import com.ourdressingtable.dressingTable.dto.CreateDressingTableResponse;
import com.ourdressingtable.dressingTable.dto.DressingTableRequest;
import com.ourdressingtable.dressingTable.service.DressingTableService;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dressing-tables")
@Tag(name = "화장대", description = "화장대 관련 API")
public class DressingTableController {

    private final DressingTableService dressingTableService;
    private final MemberService memberService;

    @PostMapping("")
    @Operation(summary = "화장대 생성", description = "새로운 화장대를 생성합니다.")
    public ResponseEntity<CreateDressingTableResponse> addDressingTable(@RequestBody @Valid DressingTableRequest dressingTableRequest,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = dressingTableService.createDressingTable(dressingTableRequest, customUserDetails.getMemberId());

        URI  location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(CreateDressingTableResponse.builder().id(id).build());

    }
}
