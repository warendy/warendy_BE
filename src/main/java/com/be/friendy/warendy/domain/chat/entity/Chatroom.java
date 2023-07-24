package com.be.friendy.warendy.domain.chat.entity;

import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE member_id=?") // delete 요청이 들어올때 db에 삭제되지 않고 deleted_at 컬럼에 삭제요청 시간으로 업데이트 된다.
@Where(clause = "deleted_at is NULL")
@Entity(name = "CHAT")
public class Chatroom extends BaseEntity {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "CHAT_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

//    @OneToOne
//    @JoinColumn(name = "BOARD_ID")

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    private String name;
    private String state;
}
