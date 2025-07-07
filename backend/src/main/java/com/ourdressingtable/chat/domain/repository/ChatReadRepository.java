package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.ChatRead;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatReadRepository extends JpaRepository<ChatRead, Long> {
    Optional<ChatRead> findByChatroomIdAndMemberId(Long chatroomId, Long memberId);
}
