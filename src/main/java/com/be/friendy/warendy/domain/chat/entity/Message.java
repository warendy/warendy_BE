package com.be.friendy.warendy.domain.chat.entity;

import com.be.friendy.warendy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "MESSAGE")
public class Message {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "MESSAGE_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CONNECTED_CHAT_ID")
    private ConnectedChat connectedChat;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String contents;
    private LocalDateTime sentAt;
}
