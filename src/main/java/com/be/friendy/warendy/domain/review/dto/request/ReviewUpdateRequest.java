package com.be.friendy.warendy.domain.review.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequest {
    private String nickname;
    private String contents;
    private Float rating;       // 유저 평점.
}
