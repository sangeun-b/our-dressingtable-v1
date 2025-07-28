package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.Chat;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.ChatroomType;
import com.ourdressingtable.chat.domain.repository.ChatRepository;
import com.ourdressingtable.chat.domain.repository.ChatroomRepository;
import com.ourdressingtable.chat.dto.ChatMemberResponse;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import com.ourdressingtable.chat.dto.CreateChatroomRequest;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.MemberService;
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
    public String createChatroom(CreateChatroomRequest request) {
        Chatroom chatroom = request.toEntity();
        return chatroomRepository.save(chatroom).getId();
    }

    @Override
    public void joinChatroom(String chatroomId) {
        String memberId = SecurityUtil.getCurrentMemberId().toString();
        if(!chatRepository.existsByChatroomIdAndMemberId(chatroomId, memberId)) {
            Chatroom chatroom = getChatroomEntityById(chatroomId);
            Member member = memberService.getMemberEntityById(Long.valueOf(memberId));
            Chat chat = Chat.builder()
                    .chatroomId(String.valueOf(chatroom.getId()))
                    .memberId(memberId)
                    .isActive(true)
                    .joinAt(LocalDateTime.now())
                    .build();
            chatRepository.save(chat);
            redisTemplate.opsForSet().add("chatroom:"+chatroomId+"members:"+memberId);
        }
    }

    @Override
    public void leaveChatroom(String chatroomId) {
        String memberId = SecurityUtil.getCurrentMemberId().toString();
        Chat chat = chatRepository.findByChatroomIdAndMemberId(chatroomId, memberId)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.CHAT_NOT_FOUND));

        if (chat.isActive()) {
            chat.updateActive(false);
        }

        String redisKey = "chatroom:" + chatroomId + "members:";
        redisTemplate.opsForSet().remove(redisKey, memberId.toString());
//        redisTemplate.opsForSet().remove("chatroom:"+chatroomId+":members"+memberId.toString());

        List<Chat> allChats = chatRepository.findAllByChatroomId(chatroomId);
        boolean isOneToOne = allChats.size() == 2;
        boolean  allInactive = allChats.stream().allMatch(c -> !c.isActive());

        if(isOneToOne && allInactive) {
            chatroomRepository.deleteById(chatroomId);
        }
    }

    @Override
    public List<ChatMemberResponse> getActiveChatMembers(String chatroomId) {
        List<Chat> chats = chatRepository.findAllByChatroomIdAndIsActiveTrue(chatroomId);
        return chats.stream()
                .map(chat -> new ChatMemberResponse(chat.getMemberId(), chat.getJoinAt()))
                .collect(Collectors.toList());
    }

    @Override
    public ChatroomResponse createOrGetOneToOneChatroom(String targetId) {
        String memberId = SecurityUtil.getCurrentMemberId().toString();

        if (memberId.equals(targetId)) {
            throw new OurDressingTableException(ErrorCode.NO_CHAT_WITH_MYSELF);
        }

        Optional<String> existedRoomId = chatRepository.findOneToOneChatroom(memberId, targetId);
        if(existedRoomId.isPresent()) {
            String chatroomId = existedRoomId.get();

            // 참여 상태 복구
            List<Chat> chats = chatRepository.findByChatroomIdAndMemberIdIn(chatroomId, List.of(memberId, targetId));

            for(Chat chat : chats) {
                if(!chat.isActive()) {
                    chat.updateActive(true);
                    chatRepository.save(chat);

                    String redisKey = "chatroom:" + chatroomId + "members:";
                    redisTemplate.opsForSet().add(redisKey, String.valueOf(memberId));
//                    redisTemplate.opsForSet().add("chatroom:"+chatroomId+"members:"+memberId.toString());
                }
            }
            Chatroom chatroom = getChatroomEntityById(chatroomId);
            return ChatroomResponse.builder().id(chatroom.getId()).name(chatroom.getName()).createdAt(chatroom.getCreatedAt()).build();
        }

        Chatroom newChatroom = Chatroom.builder()
                .name(null)
                .type(ChatroomType.ONE_TO_ONE)
                .build();
        chatroomRepository.save(newChatroom);

        List<String> members = List.of(memberId, targetId);
        for (String id: members) {
            Chat chat = Chat.builder()
                    .chatroomId(String.valueOf(newChatroom.getId()))
                    .memberId(id)
                    .isActive(true)
                    .joinAt(LocalDateTime.now())
                    .build();
            chatRepository.save(chat);
            String redisKey = "chatroom:" + newChatroom.getId() + "members:";
            redisTemplate.opsForSet().add(redisKey, id.toString());
        }
        return new ChatroomResponse(newChatroom.getId(), newChatroom.getName(), newChatroom.getCreatedAt());
    }

    @Override
    public Chatroom getChatroomEntityById(String chatroomId) {
        return chatroomRepository.findById(chatroomId).orElseThrow(() -> new OurDressingTableException(
                ErrorCode.CHATROOM_NOT_FOUND));
    }

    @Override
    public List<OneToOneChatroomSummaryResponse> getMyOneToOneChatrooms() {
        String memberId = String.valueOf(SecurityUtil.getCurrentMemberId());
        List<OneToOneChatroomSummaryResponse> chatrooms = chatroomRepository.findOneToOneChatroomsByMemberId(memberId);
        return chatrooms;
    }
}
