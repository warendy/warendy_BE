package com.be.friendy.warendy.domain.review.entity;

import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.winebar.entity.WineBar;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE member_id=?")
// delete 요청이 들어올때 db에 삭제되지 않고 deleted_at 컬럼에 삭제요청 시간으로 업데이트 된다.
@Where(clause = "deleted_at is NULL")
@Entity(name = "REVIEW")
public class Review extends BaseEntity {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "REVIEW_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "WINE_ID", nullable = false)
    private Wine wine;

    @ManyToOne
    @JoinColumn(name = "WINEBAR_ID", nullable = false)
    private WineBar wineBar;

    private String nickName;
    private String contents;
    private Double Rating;


}