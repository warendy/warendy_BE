package com.be.friendy.warendy.domain.review.dto.response;

import com.be.friendy.warendy.domain.review.entity.Review;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WineReviewSearchByWineIdResponse {

    private Long reviewId;
    String nickname;
    String contents;
    float rating;               // 유저 평점.
    private String createdAt;
    private String modifiedAt;

    public static WineReviewSearchByWineIdResponse fromEntity(Review review) {
        return WineReviewSearchByWineIdResponse.builder()
                .reviewId(review.getId())
                .nickname(review.getMember().getNickname())
                .contents(review.getContents())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }
}
