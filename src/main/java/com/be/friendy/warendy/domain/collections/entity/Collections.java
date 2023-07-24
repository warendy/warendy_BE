package com.be.friendy.warendy.domain.collections.entity;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "COLLECTIONS")
public class Collections {
    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "COLLECTION_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "WINE_ID")
    private Wine wine;
}
