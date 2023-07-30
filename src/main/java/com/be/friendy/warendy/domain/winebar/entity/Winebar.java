package com.be.friendy.warendy.domain.winebar.entity;


import com.be.friendy.warendy.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is NULL")
@Entity(name = "WINEBAR")
@Embeddable
public class Winebar extends BaseEntity {

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

}
