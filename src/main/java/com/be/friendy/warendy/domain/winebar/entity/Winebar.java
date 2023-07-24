package com.be.friendy.warendy.domain.winebar.entity;


import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.favorite.entity.Favorite;
import com.be.friendy.warendy.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE member_id=?")
// delete 요청이 들어올때 db에 삭제되지 않고 deleted_at 컬럼에 삭제요청 시간으로 업데이트 된다.
@Where(clause = "deleted_at is NULL")
@Entity(name = "WINEBAR")
@Embeddable
public class WineBar extends BaseEntity {

    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "WINEBAR_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    private String name;

    private String picture;

    private String address;

    private Double lnt;

    private Double lat;

    private Double rating;

    private Integer reviews;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "BOARD_ID")
    private List<Board> boardList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "REVIEW_ID")
    private List<Review> reviewList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "FAVORITE_ID")
    private List<Favorite> favoriteList;

}
