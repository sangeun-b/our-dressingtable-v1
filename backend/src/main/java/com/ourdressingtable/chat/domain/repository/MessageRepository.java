package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatroomIdOrderByCreatedAtDesc(String chatroomId);
}
