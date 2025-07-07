package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.ChatRead;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatReadRepository extends JpaRepository<ChatRead, Long> {
    Optional<ChatRead> findByChatroomIdAndMemberId(Long chatroomId, Long memberId);

    @Query("SELECT cr FROM ChatRead cr WHERE cr.member.id = :memberId AND cr.chatroom.id IN :chatroomIds")
    List<ChatRead> findByMemberIdAndChatroomIds(@Param("memberId") Long memberId, @Param("chatroomIds") List<Long> chatroomIds);
}
