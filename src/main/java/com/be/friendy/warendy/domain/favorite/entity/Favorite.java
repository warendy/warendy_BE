package com.be.friendy.warendy.domain.favorite.entity;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "FAVORITE")
public class Favorite {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "FAVORITE_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "WINE_ID", nullable = false)
    private Wine wine;

    private String picture;

    private String category;
}
