package com.be.friendy.warendy.domain.winebar.entity;

import com.be.friendy.warendy.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WINEBAR")
public class Winebar {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "WINEBAR_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @OneToMany
    @JoinColumn(name = "WINEBAR_ID")
    private List<Review> reviews;

    private String name;
    private String picture;
    private String address;
    private double lng;
    private double lat;
    private Float rating;
    private Integer reviewCnt;
}
