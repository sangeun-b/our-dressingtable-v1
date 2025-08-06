package com.ourdressingtable.chat.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.chat.dto.ChatMemberResponse;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import com.ourdressingtable.chat.dto.CreateOneToOneChatRequest;
import com.ourdressingtable.chat.service.ChatroomService;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.TestSecurityConfig;
import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.TestDataFactory;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = ChatroomController.class)
@DisplayName("채팅방 API 테스트")
@Import(TestSecurityConfig.class)
public class ChatroomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatroomService chatroomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("1:1 채팅방 생성 API 테스트")
    class CreateOneToOneChatroomTest {

        @DisplayName("1:1 채팅방 생성 성공 API 테스트")
        @WithCustomUser
        @Test
        public void createOneToOneChatroom_returnSuccess() throws Exception {
            CreateOneToOneChatRequest request = TestDataFactory.testCreateOneToOneChatRequest("2");
            ChatroomResponse response = TestDataFactory.testChatroomResponse("1");
            given(chatroomService.createOrGetOneToOneChatroom(any())).willReturn(response);
            mockMvc.perform(post("/api/chats/chatrooms/one-to-one")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @DisplayName("1:1 채팅방 생성 실패 API 테스트")
        @WithCustomUser
        @Test
        public void createOneToOneChatroom_returnError() throws Exception {
            CreateOneToOneChatRequest request = TestDataFactory.testCreateOneToOneChatRequest("1");

            doThrow(new OurDressingTableException(ErrorCode.NO_CHAT_WITH_MYSELF))
                    .when(chatroomService).createOrGetOneToOneChatroom(eq("1"));

            mockMvc.perform(post("/api/chats/chatrooms/one-to-one")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("자기 자신과는 채팅할 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("채팅방 나가기 API 테스트")
    class LeaveChatroomTest {

        @DisplayName("채팅방 나가기 성공 API 테스트")
        @WithCustomUser
        @Test
        public void leaveChatroom_returnSuccess() throws Exception {
            String chatroomId = "1";
           mockMvc.perform(delete("/api/chats/chatrooms/{chatroomId}/members", chatroomId))
                   .andExpect(status().isNoContent());
           verify(chatroomService).leaveChatroom(chatroomId);
        }

        @DisplayName("채팅방 나가기 실패 API 테스트 - 채팅방 미존재")
        @WithCustomUser
        @Test
        public void leaveChatroom_returnError() throws Exception {
            String chatroomId = "2";
            doThrow(new OurDressingTableException(ErrorCode.CHAT_NOT_FOUND))
                    .when(chatroomService).leaveChatroom(eq(chatroomId));

            mockMvc.perform(delete("/api/chats/chatrooms/{chatroomId}/members", chatroomId)
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("채팅방 참여 회원 API 테스트")
    class getActiveChatroomMemberTest {

        @DisplayName("채팅방 참여 회원 조회 성공 API 테스트")
        @WithCustomUser
        @Test
        public void getChatMembers_returnSuccess() throws Exception {
            String chatroomId = "1";
            ChatMemberResponse response = TestDataFactory.testChatMemberResponse("1");
            ChatMemberResponse second_response = TestDataFactory.testChatMemberResponse("2");
            List<ChatMemberResponse> members = List.of(
                    response, second_response
            );

            when(chatroomService.getActiveChatMembers(eq(chatroomId))).thenReturn(members);
            mockMvc.perform(get("/api/chats/chatrooms/{chatroomId}/members", chatroomId))
                    .andExpect(status().isOk())
                            .andExpect(jsonPath("$.length()").value(2));
        }

        @DisplayName("채팅방 참여 회원 조회 실패 API 테스트 - 미존재 채팅방")
        @WithCustomUser
        @Test
        public void getChatMembers_returnError() throws Exception {
            String chatroomId = "2";
            doThrow(new OurDressingTableException(ErrorCode.CHAT_NOT_FOUND))
                    .when(chatroomService).getActiveChatMembers(eq(chatroomId));

            mockMvc.perform(get("/api/chats/chatrooms/{chatroomId}/members", chatroomId)
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }
    }

}
