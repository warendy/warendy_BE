package com.be.friendy.warendy.domain.review.dto.response;

import com.be.friendy.warendy.domain.review.entity.Review;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WineReviewCreateResponse {
    String nickname;
    String wineName;
    String contents;
    Float rating;           // 유저 평점.
    private String createdAt;
    private String modifiedAt;

    public static WineReviewCreateResponse fromEntity(Review review) {
        return WineReviewCreateResponse.builder()
                .nickname(review.getMember().getNickname())
                .wineName(review.getWine().getName())
                .contents(review.getContents())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }
}
