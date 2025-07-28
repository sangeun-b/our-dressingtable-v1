package com.ourdressingtable.chat.domain.repository;

import java.util.Optional;

public interface ChatRepositoryCustom {
    Optional<String> findOneToOneChatroom(String memberId, String targetId);
}
