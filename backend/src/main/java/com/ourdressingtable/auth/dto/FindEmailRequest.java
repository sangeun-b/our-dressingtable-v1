package com.ourdressingtable.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class FindEmailRequest {

    @Schema(description = "이름", example = "김이름")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(description = "전화번호", example = "010-1234-5678")
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    @Builder
    public FindEmailRequest(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

}
