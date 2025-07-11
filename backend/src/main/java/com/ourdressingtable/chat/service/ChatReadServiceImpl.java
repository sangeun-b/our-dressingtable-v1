package com.ourdressingtable.chat.service;

import com.ourdressingtable.OurDressingTableApplication;
import com.ourdressingtable.chat.domain.ChatRead;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.repository.ChatReadRepository;
import com.ourdressingtable.chat.domain.repository.ChatroomRepository;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
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
    public void markAsRead(Long chatroomId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Chatroom chatroom = chatroomService.getChatroomEntityById(chatroomId);
        Member member = memberService.getMemberEntityById(memberId);

        LocalDateTime now = LocalDateTime.now();

        ChatRead chatRead = chatReadRepository.findByChatroomIdAndMemberId(chatroomId, memberId)
                .map(cr -> {
                    cr.updateReadAt(now);
                    return cr;
                })
                .orElse(ChatRead.builder()
                        .chatroom(chatroom)
                        .member(member)
                        .lastReadAt(now)
                        .build());
        chatReadRepository.save(chatRead);
    }

    @Override
    public LocalDateTime getLastReadAt(Long chatroomId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        LocalDateTime lastReadAt = chatReadRepository.findByChatroomIdAndMemberId(chatroomId, memberId)
                .map(ChatRead::getLastReadAt)
                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0)); // 한번도 안 읽었으면 가장 예전 시간으로 설정
        return lastReadAt;
    }
}
