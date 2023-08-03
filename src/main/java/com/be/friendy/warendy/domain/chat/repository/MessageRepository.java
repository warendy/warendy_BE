package com.be.friendy.warendy.domain.chat.repository;

import com.be.friendy.warendy.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByRoomIdOrderByTimestamp(String roomId);
}
