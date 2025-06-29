package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Chat;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom{
    boolean existsByChatroomIdAndMemberId(Long chatroomId, Long memberId);
    Optional<Chat> findByChatroomIdAndMemberId(Long chatroomId, Long memberId);
    List<Chat> findAllByChatroomIdAndIsActiveTrue(Long chatroomId);
    List<Chat> findByChatroomIdAndMemberIdIn(Long chatroomId, List<Long> memberIds);
}
