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

    String wineName;
    String winePicture;
    String contents;
    float rating;                   // 유저 평점.
    private String createdAt;
    private String modifiedAt;

    public static MyReviewSearchResponse fromEntity(Review review) {
        return MyReviewSearchResponse.builder()
                .wineName(review.getWine().getName())
                .winePicture(review.getWine().getPicture())
                .contents(review.getContents())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }
}
