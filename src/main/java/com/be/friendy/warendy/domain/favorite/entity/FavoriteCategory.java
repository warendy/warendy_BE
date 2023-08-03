package com.be.friendy.warendy.domain.favorite.entity;


import com.be.friendy.warendy.domain.member.entity.Member;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FavoriteCategory {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "CATEGORY_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "FAVORITE_ID", nullable = false)
//    private Favorite favorite;
//
//    @ManyToOne
//    @JoinColumn(name = "MEMBER_ID", nullable = false)
//    private Member member;

    private String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, String> wineIds;
}
