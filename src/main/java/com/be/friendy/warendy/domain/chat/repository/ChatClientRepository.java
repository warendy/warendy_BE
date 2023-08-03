package com.be.friendy.warendy.domain.chat.repository;

import com.be.friendy.warendy.domain.chat.entity.ChatClient;
import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import com.be.friendy.warendy.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatClientRepository extends JpaRepository<ChatClient, Long> {
    List<ChatClient> findByChatRoom(ChatRoom chatRoom);

    ChatClient findByChatRoomAndMember(ChatRoom chatRoom, Member member);

}
