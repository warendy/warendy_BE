package com.be.friendy.warendy.domain.notification.entity;

import com.be.friendy.warendy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "NOTIFICATION")
@EqualsAndHashCode(of = "id")
public class Notification {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "NOTIFICATION_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member receiver;

    @Builder
    public Notification(Member receiver, String content, Boolean isRead) {
        this.receiver = receiver;
        this.content = content;
        this.isRead = isRead;
    }

}
