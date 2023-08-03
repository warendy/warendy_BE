package com.be.friendy.warendy.domain.chat.repository;

import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    ChatRoom findByName(String name);

    ChatRoom findByRoomId(String roomId);
}
