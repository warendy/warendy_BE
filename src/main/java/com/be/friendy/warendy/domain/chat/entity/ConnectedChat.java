package com.be.friendy.warendy.domain.chat.entity;


import com.be.friendy.warendy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CONNECTED_CHAT")
public class ConnectedChat {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "CONNECTED_CHAT_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "CHATROOM_ID", nullable = false)
    private Chatroom chatroom;

}
