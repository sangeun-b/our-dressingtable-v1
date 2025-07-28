package com.ourdressingtable.chat.component;

import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.Message;
import com.ourdressingtable.chat.domain.repository.ChatroomRepository;
import com.ourdressingtable.chat.domain.repository.MessageRepository;
import com.ourdressingtable.chat.dto.ChatMessage;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaChatConsumer {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat-message", groupId = "chat-consumer")
    public void listen(@Payload ChatMessage chatMessage) {
        log.info("[Kafka 수신] {}" + chatMessage);

        Member sender = memberRepository.findById(Long.valueOf(chatMessage.getSenderId()))
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));
        Chatroom chatroom = chatroomRepository.findById(chatMessage.getChatroomId())
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.CHATROOM_NOT_FOUND));

        Message message = chatMessage.toEntity(chatroom.getId(), String.valueOf(sender.getId()));

        messageRepository.save(message);

        // web-socket 으로 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatroomId(), chatMessage);
    }
}
