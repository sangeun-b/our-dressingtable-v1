package com.ourdressingtable.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ChatroomEnterResponse(
        OneToOneChatroomSummaryResponse chatroomResponse,
        List<MessageResponse> messages,
        boolean hasNext
) {
}
