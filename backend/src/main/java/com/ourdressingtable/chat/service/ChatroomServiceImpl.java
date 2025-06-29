package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.Chat;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.repository.ChatRepository;
import com.ourdressingtable.chat.domain.repository.ChatroomRepository;
import com.ourdressingtable.chat.dto.ChatMemberResponse;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import com.ourdressingtable.chat.dto.CreateChatroomRequest;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatroomServiceImpl implements ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    @Transactional
    public Long createChatroom(CreateChatroomRequest request) {
        Chatroom chatroom = request.toEntity();
        return chatroomRepository.save(chatroom).getId();
    }

    @Override
    @Transactional
    public void joinChatroom(Long chatroomId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if(!chatRepository.existsByChatroomIdAndMemberId(chatroomId, memberId)) {
            Chatroom chatroom = getChatroomEntityById(chatroomId);
            Member member = memberService.getMemberEntityById(memberId);
            Chat chat = Chat.builder()
                    .chatroom(chatroom)
                    .member(member)
                    .isActive(true)
                    .joinAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            chatRepository.save(chat);
            redisTemplate.opsForSet().add("chatroom:"+chatroomId+"members:"+memberId.toString());
        }
    }

    @Override
    public void leaveChatroom(Long chatroomId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Chat chat = chatRepository.findByChatroomIdAndMemberId(chatroomId, memberId)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.CHAT_NOT_FOUND));

        if (chat.isActive()) {
            chat.updateActive(false);
        }

        redisTemplate.opsForSet().remove("chatroom:"+chatroomId+"members:"+memberId.toString());
    }

    @Override
    public List<ChatMemberResponse> getActiveChatMembers(Long chatroomId) {
        List<Chat> chats = chatRepository.findAllByChatroomIdAndIsActiveTrue(chatroomId);
        return chats.stream()
                .map(chat -> new ChatMemberResponse(chat.getMember().getId(), chat.getJoinAt()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatroomResponse createOrGetOneToOneChatroom(Long targetId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        if (memberId.equals(targetId)) {
            throw new OurDressingTableException(ErrorCode.NO_CHAT_WITH_MYSELF);
        }

        Optional<Long> existedRoomId = chatRepository.findOneToOneChatroom(memberId, targetId);
        if(existedRoomId.isPresent()) {
            Long chatroomId = existedRoomId.get();

            // 참여 상태 복구
            List<Chat> chats = chatRepository.findByChatroomIdAndMemberIdIn(chatroomId, List.of(memberId, targetId));

            for(Chat chat : chats) {
                if(!chat.isActive()) {
                    chat.updateActive(true);
                    chatRepository.save(chat);

                    String redisKey = "chatroom:" + chatroomId + ":members";
                    redisTemplate.opsForSet().add(redisKey, memberId.toString());
//                    redisTemplate.opsForSet().add("chatroom:"+chatroomId+"members:"+memberId.toString());
                }
            }
            Chatroom chatroom = getChatroomEntityById(existedRoomId.get());
            return ChatroomResponse.builder().id(chatroom.getId()).name(chatroom.getName()).createdAt(chatroom.getCreatedAt()).build();
        }

        Chatroom newChatroom = Chatroom.builder()
                .name(null)
                .build();
        chatroomRepository.save(newChatroom);

        List<Long> members = List.of(memberId, targetId);
        for (Long id: members) {
            Chat chat = Chat.builder()
                    .chatroom(newChatroom)
                    .member(memberService.getMemberEntityById(id))
                    .isActive(true)
                    .joinAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            chatRepository.save(chat);
            String redisKey = "chatroom:" + newChatroom.getId() + ":members";
            redisTemplate.opsForSet().add(redisKey, id.toString());
        }
        return new ChatroomResponse(newChatroom.getId(), newChatroom.getName(), newChatroom.getCreatedAt());
    }

    @Override
    public Chatroom getChatroomEntityById(Long chatroomId) {
        return chatroomRepository.findById(chatroomId).orElseThrow(() -> new OurDressingTableException(
                ErrorCode.CHATROOM_NOT_FOUND));
    }
}
