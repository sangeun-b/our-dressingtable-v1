package com.ourdressingtable.dressingTable.controller;

import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.dressingTable.dto.CreateDressingTableResponse;
import com.ourdressingtable.dressingTable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingTable.service.DressingTableService;
import com.ourdressingtable.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dressing-tables")
@Tag(name = "화장대", description = "화장대 관련 API")
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "화장대 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "403", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
})
public class DressingTableController {

    private final DressingTableService dressingTableService;
    private final MemberService memberService;

    @PostMapping()
    @Operation(summary = "화장대 생성", description = "새로운 화장대를 생성합니다.")
    public ResponseEntity<CreateDressingTableResponse> addDressingTable(@RequestBody @Valid CreateDressingTableRequest dressingTableRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Long id = dressingTableService.createDressingTable(dressingTableRequest, currentMemberId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(CreateDressingTableResponse.builder().id(id).build());

    }

    @PatchMapping("/{id}")
    @Operation(summary = "화장대 수정", description = "기존 화장대를 수정합니다.")
    public ResponseEntity updateDressingTable(@PathVariable Long id, @RequestBody @Valid CreateDressingTableRequest dressingTableRequest) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        dressingTableService.updateDressingTable(dressingTableRequest, id, currentMemberId);
        return ResponseEntity.noContent().build();
    }
}
