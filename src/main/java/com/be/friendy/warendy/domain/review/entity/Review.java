package com.be.friendy.warendy.domain.review.entity;

import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.review.dto.request.ReviewUpdateRequest;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE review SET deleted_at = NOW() WHERE review_id=?")
// delete 요청이 들어올때 db에 삭제되지 않고 deleted_at 컬럼에 삭제요청 시간으로 업데이트 된다.
@Where(clause = "deleted_at is NULL")
@Entity(name = "REVIEW")
public class Review extends BaseEntity {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "REVIEW_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WINE_ID", nullable = false)
    private Wine wine;

    private String contents;
    private Float rating;           // 유저 평점.

    public void updateReviewInfo(ReviewUpdateRequest reviewUpdateRequest) {
        contents = reviewUpdateRequest.getContents();
        rating = reviewUpdateRequest.getRating();
    }
}
