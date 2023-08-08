package com.be.friendy.warendy.domain.winebar.dto.response;


import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinebarSearchResponse {

    private String name;

    private String picture;

    private String address;

    private Double lnt;

    private Double lat;

    private Double rating;

    private Integer reviews;

    public static WinebarSearchResponse fromEntity(Winebar winebar) {
        return WinebarSearchResponse.builder()
                .name(winebar.getName())
                .picture(winebar.getPicture())
                .address(winebar.getAddress())
                .lnt(winebar.getLnt())
                .lat(winebar.getLat())
                .rating(winebar.getRating())
                .reviews(winebar.getReviews())
                .build();
    }

}
