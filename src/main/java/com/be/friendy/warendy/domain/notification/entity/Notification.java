package com.be.friendy.warendy.domain.notification.entity;

import com.be.friendy.warendy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EqualsAndHashCode(of = "id")
public class Notification {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "NOTIFICATION_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    private String boardId;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member receiver;

    @Builder
    public Notification(String boardId, String content, Boolean isRead, Member receiver) {
        this.receiver = receiver;
        this.boardId = boardId;
        this.content = content;
        this.isRead = isRead;
    }
}
