package com.be.friendy.warendy.domain.chat.repository;

import com.be.friendy.warendy.domain.chat.entity.ChatClient;
import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import com.be.friendy.warendy.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatClientRepository extends JpaRepository<ChatClient, Long> {
    Optional<List<ChatClient>> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatClient> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

}
