package com.ourdressingtable.chat.service;

import com.ourdressingtable.chat.domain.ChatRead;
import com.ourdressingtable.chat.domain.repository.ChatReadRepository;
import com.ourdressingtable.common.util.SecurityUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatReadQueryServiceImpl implements ChatReadQueryService {

    private final ChatReadRepository chatReadRepository;
    private static final LocalDateTime SAFE_MIN_DATETIME = LocalDateTime.of(1970, 1, 1, 0, 0);

    @Override
    public LocalDateTime getLastReadAt(String chatroomId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        // 한번도 안 읽었으면 가장 예전 시간으로 설정
        return chatReadRepository.findByChatroomIdAndMemberId(chatroomId, String.valueOf(memberId))
                .map(ChatRead::getLastReadAt)
                .orElse(SAFE_MIN_DATETIME);
    }

}
