package com.ourdressingtable.chat.domain.repository;

import java.util.Optional;

public interface ChatRepositoryCustom {
    Optional<Long> findOneToOneChatroom(Long memberId, Long targetId);
}
