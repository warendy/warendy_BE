package com.be.friendy.warendy.domain.winebar.dto.response;


import com.be.friendy.warendy.domain.board.dto.response.BoardSearchResponse;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinebarSearchResponse {

    private Long winebarId;

    private String name;

    private String picture;

    private String region;

    private String address;

    private Double lnt;

    private Double lat;

    private Double rating;

    private Integer reviews;

    private List<BoardSearchResponse> boardList;

    public static WinebarSearchResponse fromEntity(Winebar winebar) {
        String address = winebar.getAddress();
        String regionFromAddress = address.split(" ")[0];

        return WinebarSearchResponse.builder()
                .winebarId(winebar.getId())
                .name(winebar.getName())
                .picture(winebar.getPicture())
                .region(regionFromAddress)
                .address(address)
                .lnt(winebar.getLnt())
                .lat(winebar.getLat())
                .rating(winebar.getRating())
                .reviews(winebar.getReviews())
                .boardList(winebar.getBoardList()
                        .stream().map(BoardSearchResponse::fromEntity)
                        .toList())
                .build();
    }

}
