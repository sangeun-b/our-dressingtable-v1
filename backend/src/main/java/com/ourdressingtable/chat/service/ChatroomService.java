package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.dto.ChatMemberResponse;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import com.ourdressingtable.chat.dto.CreateChatroomRequest;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;
import java.util.List;

public interface ChatroomService {

    String createChatroom(CreateChatroomRequest request);
    void joinChatroom(String chatroomId);
    Chatroom getChatroomEntityById(String chatroomId);
    void leaveChatroom(String chatroomId);
    List<ChatMemberResponse> getActiveChatMembers(String chatroomId);
    ChatroomResponse createOrGetOneToOneChatroom(String targetId);;
    List<OneToOneChatroomSummaryResponse> getMyOneToOneChatrooms();
}
