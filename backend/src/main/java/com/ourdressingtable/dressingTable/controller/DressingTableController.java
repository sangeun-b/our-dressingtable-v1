package com.ourdressingtable.dressingtable.controller;

import com.ourdressingtable.dressingtable.dto.CreateDressingTableResponse;
import com.ourdressingtable.dressingtable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingtable.dto.DressingTableDetailResponse;
import com.ourdressingtable.dressingtable.dto.DressingTableResponse;
import com.ourdressingtable.dressingtable.dto.UpdateDressingTableRequest;
import com.ourdressingtable.dressingtable.service.DressingTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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

public class DressingTableController {

    private final DressingTableService dressingTableService;

    @PostMapping()
    @Operation(summary = "화장대 생성", description = "새로운 화장대를 생성합니다.", security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "화장대 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<CreateDressingTableResponse> addDressingTable(@RequestBody @Valid CreateDressingTableRequest dressingTableRequest) {
        Long id = dressingTableService.createDressingTable(dressingTableRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(CreateDressingTableResponse.builder().id(id).build());

    }

    @PatchMapping("/{id}")
    @Operation(summary = "화장대 수정", description = "기존 화장대를 수정합니다.", security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "화장대 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity updateDressingTable(@PathVariable Long id, @RequestBody @Valid UpdateDressingTableRequest dressingTableRequest) {
        dressingTableService.updateDressingTable(dressingTableRequest, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "화장대 삭제", description = "사용자의 화장대를 삭제합니다.", security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "화장대 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity deleteDressingTable(@PathVariable Long id) {
        dressingTableService.deleteDressingTable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    @Operation(summary = "내 화장대 조회", description = "사용자의 화장대를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 화장대 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<DressingTableResponse>> getMyDressingTable() {
        return ResponseEntity.ok(dressingTableService.getAllMyDressingTables());
    }

    @GetMapping("/{id}")
    @Operation(summary = "화장대 상세 조회", description = "화장대 상세 조회를 합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "화장대 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<DressingTableDetailResponse> getDressingTableDetail(@PathVariable Long id) {
        return ResponseEntity.ok(dressingTableService.getDressingTableDetail(id));
    }
}
