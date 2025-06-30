package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.dto.ChatMemberResponse;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import com.ourdressingtable.chat.dto.CreateChatroomRequest;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;
import java.util.List;

public interface ChatroomService {

    Long createChatroom(CreateChatroomRequest request);
    void joinChatroom(Long chatroomId);
    Chatroom getChatroomEntityById(Long chatroomId);
    void leaveChatroom(Long chatroomId);
    List<ChatMemberResponse> getActiveChatMembers(Long chatroomId);
    ChatroomResponse createOrGetOneToOneChatroom(Long targetId);;
    List<OneToOneChatroomSummaryResponse> getMyOneToOneChatrooms();
}
