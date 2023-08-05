package com.be.friendy.warendy.domain.chat.repository;

import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
//    ChatRoom findByName(String name);

    Optional<ChatRoom> findByRoomId(String roomId);
}
