package com.ourdressingtable.chat.controller;

import com.ourdressingtable.chat.service.ChatReadService;
import com.ourdressingtable.chat.service.KafkaChatProducer;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatMessageController.class)
@ActiveProfiles("test")
@DisplayName("메세지 읽음 API 테스트")
@Import(TestSecurityConfig.class)
public class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatReadService chatReadService;

    @MockBean
    private KafkaChatProducer kafkaChatProducer;

    @Nested
    @DisplayName("메세지 읽음 처리 API 테스트")
    class ReadMessageApiTest {

        @DisplayName("메세지 읽음 처리 성공 API 테스트")
        @Test
        public void readMessage_returnSuccess() throws Exception {
            Long chatroomId = 1L;

            mockMvc.perform(patch("/api/chatrooms/{chatroomId}/read", chatroomId))
                    .andExpect(status().isOk());

            verify(chatReadService).markAsRead(chatroomId);
        }

        @DisplayName("메세지 읽음 처리 실패 API 테스트")
        @Test
        public void readMessage_returnError() throws Exception {
            Long chatroomId = 10L;

            doThrow(new OurDressingTableException(ErrorCode.CHATROOM_NOT_FOUND)).when(chatReadService).markAsRead(chatroomId);
            mockMvc.perform(patch("/api/chatrooms/{chatroomId}/read", chatroomId))
                    .andExpect(status().isNotFound());

        }
    }

}
