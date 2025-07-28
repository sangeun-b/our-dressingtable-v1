package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.ChatRead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatReadRepository extends MongoRepository<ChatRead, String> {

    Optional<ChatRead> findByChatroomIdAndMemberId(String chatroomId, String memberId);
    List<ChatRead> findByMemberIdAndChatroomIdIn(String memberId, List<String> chatroomIds);
}