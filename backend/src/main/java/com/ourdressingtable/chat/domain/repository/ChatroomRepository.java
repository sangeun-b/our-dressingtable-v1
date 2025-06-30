package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom,Long>, ChatroomRepositoryCustom {

}
