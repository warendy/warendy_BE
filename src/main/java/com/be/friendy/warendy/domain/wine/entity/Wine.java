package com.be.friendy.warendy.domain.wine.entity;

import com.be.friendy.warendy.domain.collections.entity.Collections;
import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE member_id=?") // delete 요청이 들어올때 db에 삭제되지 않고 deleted_at 컬럼에 삭제요청 시간으로 업데이트 된다.
@Where(clause = "deleted_at is NULL")
@Entity(name = "WINE")
public class Wine extends BaseEntity {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "WINE_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @OneToMany
    @JoinColumn(name = "WINE_ID")
    private List<Review> reviews;

    @OneToMany
    @JoinColumn(name = "WINE_ID")
    private List<Collections> collections;


    private String name;
    private int vintage;
    private String price;
    private String picture;
    private Integer body;
    private Integer dry;
    private Integer tannin;
    private Integer acidity;
    private double alcohol;
    private String grapes;
    private String region;
    private String type;
    private String winery;
    private Float rating;
}
