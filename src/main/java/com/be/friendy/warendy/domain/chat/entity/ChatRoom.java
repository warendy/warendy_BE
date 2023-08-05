package com.be.friendy.warendy.domain.chat.entity;

import com.be.friendy.warendy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CHATROOM")
public class ChatRoom implements Serializable {
    @Id // 엔티티 내부에서 아이디임을 선언
    @Column(name = "ROOM_ID") // 아이디에 해당하는 컬럼명 선언
    private String roomId;
    @ManyToOne
    @JoinColumn(name = "email")
    private Member creator;
    private String name;
    private Integer memberNum;
}
