package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends MongoRepository<Chatroom,String>, ChatroomRepositoryCustom {

}
