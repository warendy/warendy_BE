package com.be.friendy.warendy.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "WINE")
public class Wine {

    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "WINE_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    private String name;
    private int vintage;
    private String price;
    private String picture;
    private int body;
    private int dry;
    private int tannin;
    private int acidity;
    private double alcohol;
    private String grapes;
    private String region;
    private String type;
    private String winery;
    private float rating;


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

}
