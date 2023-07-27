package com.be.friendy.warendy.domain.wine.entity;

import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.favorite.entity.Favorite;
import com.be.friendy.warendy.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WINE")
public class Wine extends BaseEntity {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "WINE_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @OneToMany
    @JoinColumn(name = "REVIEW_ID")
    private List<Review> reviews;

    @OneToMany
    @JoinColumn(name = "FAVORITE_ID")
    private List<Favorite> favorites;

    private String name;
    private Integer vintage;
    private String price;
    private String picture;
    private Integer body;
    private Integer dry;
    private Integer tannin;
    private Integer acidity;
    private Double alcohol;
    private String grapes;
    private String region;
    private String type;
    private String winery;
    private Float rating;

}
