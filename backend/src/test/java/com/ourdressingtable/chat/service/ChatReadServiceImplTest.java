package com.ourdressingtable.chat.service;

import com.ourdressingtable.auth.dto.CustomUserDetails;
import com.ourdressingtable.chat.domain.ChatRead;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.repository.ChatReadRepository;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.common.util.SecurityUtilMockHelper;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("메시지 읽음 테스트")
public class ChatReadServiceImplTest {

    @InjectMocks
    private ChatReadServiceImpl chatReadService;

    @Mock
    private ChatReadRepository chatReadRepository;

    @Mock
    private ChatroomService chatroomService;

    @Mock
    private MemberService memberService;

    @Nested
    @DisplayName("메시지 읽음 테스트")
    class ReadMessageTest {

        @DisplayName("기존 ChatRead 있으면 업데이트")
        @Test
        public void readMessage_returnUpdate() {
            try (MockedStatic<SecurityUtil> mockedStatic = SecurityUtilMockHelper.mockCurrentMemberId(1L)) {
                Member member = TestDataFactory.testMember(1L);
                Chatroom chatroom = TestDataFactory.testChatroom("1");
                ChatRead chatRead = TestDataFactory.tesChatRead(String.valueOf(member.getId()), chatroom.getId());

                given(chatroomService.getChatroomEntityById("1")).willReturn(chatroom);
                given(memberService.getMemberEntityById(1L)).willReturn(member);
                given(chatReadRepository.findByChatroomIdAndMemberId("1", "1")).willReturn(Optional.of(chatRead));

                chatReadService.markAsRead(chatroom.getId());

                assertThat(chatRead.getLastReadAt().isBefore(LocalDateTime.now()));
                verify(chatReadRepository).save(chatRead);
            }
        }

        @DisplayName("기존 ChatRead 없으면 생성")
        @Test
        public void readMessage_returnCreate() {
            try (MockedStatic<SecurityUtil> mockedStatic = SecurityUtilMockHelper.mockCurrentMemberId(1L)) {
                Member member = TestDataFactory.testMember(1L);
                Chatroom chatroom = TestDataFactory.testChatroom("1");

                given(chatroomService.getChatroomEntityById("1")).willReturn(chatroom);
                given(memberService.getMemberEntityById(1L)).willReturn(member);
                given(chatReadRepository.findByChatroomIdAndMemberId("1", "1")).willReturn(Optional.empty());

                chatReadService.markAsRead(chatroom.getId());

                verify(chatReadRepository).save(any(ChatRead.class));
            }
        }
    }

    @Test
    @DisplayName("마지막 읽은 날짜없으면 MIN")
    void getLastReadAt_returnMIN() {
        try (MockedStatic<SecurityUtil> mockedStatic = SecurityUtilMockHelper.mockCurrentMemberId(1L)) {
            given(chatReadRepository.findByChatroomIdAndMemberId("1", "1")).willReturn(Optional.empty());

            LocalDateTime lastReadAt = chatReadService.getLastReadAt("1");
            assertThat(lastReadAt).isEqualTo(LocalDateTime.MIN);
        }
    }
}
