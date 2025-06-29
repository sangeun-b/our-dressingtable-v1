package com.ourdressingtable.chat.dto;

import com.ourdressingtable.chat.domain.Chatroom;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "채팅방 생성 요청 DTO")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateChatroomRequest {

    @Schema(description = "채팅방 이름", example = "우리의 첫 채팅방")
    @NotBlank(message = "채팅방 이름은 필수입니다.")
    private String name;

    @Builder
    public CreateChatroomRequest(String name){
        this.name = name;
    }

    public Chatroom toEntity() {
        return Chatroom.builder()
                .name(this.name)
                .build();
    }
}
