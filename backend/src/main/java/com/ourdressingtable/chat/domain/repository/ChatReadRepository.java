package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.ChatRead;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatReadRepository extends MongoRepository<ChatRead, String> {

    Optional<ChatRead> findByChatroomIdAndMemberId(String chatroomId, String memberId);
    List<ChatRead> findByMemberIdAndChatroomIdIn(String memberId, List<String> chatroomIds);
}