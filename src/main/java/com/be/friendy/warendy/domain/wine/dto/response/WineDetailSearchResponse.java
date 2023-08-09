package com.be.friendy.warendy.domain.wine.dto.response;

import com.be.friendy.warendy.domain.review.dto.response.WineReviewSearchByWineIdResponse;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WineDetailSearchResponse {
    private Long id;
    private String wineName;
    private Integer vintage;
    private String price;
    private String picture;
    private Integer body;
    private Integer dry;
    private Integer tannin;
    private Integer acidity;
    private Double alcohol;
    private String grapes;
    private String pairing;
    private String region;
    private String type;
    private String winery;
    private Float rating;
    private List<WineReviewSearchByWineIdResponse> reviewList;
    private String country;

    public static WineDetailSearchResponse fromEntity(Wine wine) {
        String country = wine.getRegion().split("/")[0].trim();

        List<WineReviewSearchByWineIdResponse> reviewList = wine.getReviewList()
                .stream()
                .map(WineReviewSearchByWineIdResponse::fromEntity).toList();

        return WineDetailSearchResponse.builder()
                .id(wine.getId())
                .wineName(wine.getName())
                .vintage(wine.getVintage())
                .price(wine.getPrice())
                .picture(wine.getPicture())
                .body(wine.getBody())
                .dry(wine.getDry())
                .tannin(wine.getTannin())
                .acidity(wine.getAcidity())
                .alcohol(wine.getAlcohol())
                .grapes(wine.getGrapes())
                .pairing(wine.getPairing())
                .region(wine.getRegion())
                .type(wine.getType())
                .winery(wine.getWinery())
                .rating(wine.getRating())
                .reviewList(reviewList)
                .country(country)
                .build();
    }
}
