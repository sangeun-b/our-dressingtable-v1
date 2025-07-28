package com.ourdressingtable.chat.dto;

import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.Message;
import com.ourdressingtable.chat.domain.MessageType;
import com.ourdressingtable.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "채팅 메시지 DTO")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Schema(description = "메시지 유형 (text, image, file, system 등)", example = "text")
    private MessageType MessageType;

    @Schema(description = "채팅방 ID", example = "1")
    @NotNull(message = "채팅방 ID는 필수입니다.")
    private String chatroomId;

    @Schema(description = "발신자의 회원 ID", example = "1")
    @NotNull(message = "발신자 ID는 필수 입니다.")
    private String senderId;

    @Schema(description = "메시지 내용", example = "반가워!")
    @NotBlank(message = "메시지 내용을 입력해주세요.")
    private String content;

    @Builder
    public ChatMessage(MessageType MessageType, String chatroomId, String senderId, String content) {
        this.MessageType = MessageType;
        this.chatroomId = chatroomId;
        this.senderId = senderId;
        this.content = content;
    }

    public Message toEntity(String chatroomId, String senderId) {
        return Message.builder()
                .messageType(MessageType)
                .chatroomId(chatroomId)
                .senderId(senderId)
                .content(content)
                .build();
    }


}
