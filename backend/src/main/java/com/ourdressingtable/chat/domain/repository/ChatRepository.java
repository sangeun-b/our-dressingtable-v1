package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String>, ChatRepositoryCustom{
    boolean existsByChatroomIdAndMemberId(String chatroomId, String memberId);
    Optional<Chat> findByChatroomIdAndMemberId(String chatroomId, String memberId);
    List<Chat> findAllByChatroomIdAndIsActiveTrue(String chatroomId);
    List<Chat> findByChatroomIdAndMemberIdIn(String chatroomId, List<String> memberIds);
    List<Chat> findAllByChatroomId(String chatroomId);
    List<Chat> findByChatroomIdAndIsActiveTrue(String chatroomId);

}
