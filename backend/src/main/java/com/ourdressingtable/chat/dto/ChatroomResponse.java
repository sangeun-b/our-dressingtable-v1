package com.ourdressingtable.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "채팅방 응답 DTO")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomResponse {

    @Schema(description = "생성된 채팅방 ID", example = "1")
    private String id;

    @Schema(description = "채팅방 이름", example = "우리의 첫 채팅방")
    private String name;

    @Schema(description = "채팅방 생성 시각")
    private LocalDateTime createdAt;

    @Builder
    public ChatroomResponse(String id, String name, LocalDateTime createdAt){
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

}
