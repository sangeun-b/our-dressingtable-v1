package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;
import java.util.List;

public interface ChatroomRepositoryCustom {
    List<OneToOneChatroomSummaryResponse> findOneToOneChatroomsByMemberId(String memberId);
}
