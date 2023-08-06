package com.be.friendy.warendy.domain.review.dto.response;

import com.be.friendy.warendy.domain.review.entity.Review;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateResponse {
    private String nickname;
    private String contents;
    private Float rating;       // 유저 평점.
    private String modifiedAt;

    public static ReviewUpdateResponse fromEntity(Review review) {
        return ReviewUpdateResponse.builder()
                .nickname(review.getMember().getNickname())
                .contents(review.getContents())
                .rating(review.getRating())
                .modifiedAt(review.getModifiedAt())
                .build();
    }
}
