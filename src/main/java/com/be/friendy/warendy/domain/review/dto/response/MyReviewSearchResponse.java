package com.be.friendy.warendy.domain.review.dto.response;

import com.be.friendy.warendy.domain.review.entity.Review;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyReviewSearchResponse {

    private Long reviewId;
    private Long wineId;
    private String wineName;
    private String winePicture;
    private String wineType;
    private String winePairing;
    private Float wineRating;
    private String contents;
    private Float rating;                   // 유저 평점.
    private String createdAt;
    private String modifiedAt;

    public static MyReviewSearchResponse fromEntity(Review review) {
        return MyReviewSearchResponse.builder()
                .reviewId(review.getId())
                .wineId(review.getWine().getId())
                .wineName(review.getWine().getName())
                .winePicture(review.getWine().getPicture())
                .wineType(review.getWine().getType())
                .winePairing(review.getWine().getPairing())
                .wineRating(review.getWine().getRating())
                .contents(review.getContents())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }
}
