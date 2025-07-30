package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.ChatRead;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.repository.ChatReadRepository;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatReadServiceImpl implements ChatReadService {

    private final ChatReadRepository chatReadRepository;
    private final ChatroomService chatroomService;
    private final MemberService memberService;

    @Override
    @Transactional
    public void markAsRead(String chatroomId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Chatroom chatroom = chatroomService.getChatroomEntityById(chatroomId);
        Member member = memberService.getMemberEntityById(memberId);

        LocalDateTime now = LocalDateTime.now();

        ChatRead chatRead = chatReadRepository.findByChatroomIdAndMemberId(chatroomId, String.valueOf(memberId))
                .map(cr -> {
                    cr.updateReadAt(now);
                    return cr;
                })
                .orElse(ChatRead.builder()
                        .chatroomId(String.valueOf(chatroom.getId()))
                        .memberId(String.valueOf(memberId))
                        .lastReadAt(now)
                        .build());
        chatReadRepository.save(chatRead);
    }
}
