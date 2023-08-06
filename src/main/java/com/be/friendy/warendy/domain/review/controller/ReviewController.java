package com.be.friendy.warendy.domain.review.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.review.dto.request.ReviewUpdateRequest;
import com.be.friendy.warendy.domain.review.dto.request.WineReviewCreateRequest;
import com.be.friendy.warendy.domain.review.dto.response.MyReviewSearchResponse;
import com.be.friendy.warendy.domain.review.dto.response.ReviewUpdateResponse;
import com.be.friendy.warendy.domain.review.dto.response.WineReviewCreateResponse;
import com.be.friendy.warendy.domain.review.dto.response.WineReviewSearchByWineIdResponse;
import com.be.friendy.warendy.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final TokenProvider tokenProvider;

    @PostMapping("/wines/{wine-id}")
    public ResponseEntity<WineReviewCreateResponse> wineReviewCreate(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable(value = "wine-id") Long wineId,
            @RequestBody WineReviewCreateRequest wineReviewCreateRequest
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(
                reviewService.createWineReview(email, wineId, wineReviewCreateRequest));
    }

    @GetMapping("/wines/{wine-id}")
    public ResponseEntity<Page<WineReviewSearchByWineIdResponse>>
    wineReviewSearchByWineId(
            @PathVariable(value = "wine-id") Long wineId,
            @PageableDefault(size = 3) Pageable pageable) {

        return ResponseEntity.ok(
                reviewService.searchWineReviewByWineId(wineId, pageable));
    }

    @GetMapping("/my-review/{nickname}")
    public ResponseEntity<Page<MyReviewSearchResponse>> MyReviewSearch(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String nickname,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(
                reviewService.searchMyReview(email, nickname, pageable));
    }

    @PutMapping("/{review-id}")
    public ResponseEntity<ReviewUpdateResponse> reviewUpdate(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable(value = "review-id") Long reviewId,
            @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(
                reviewService.updateReview(email, reviewId, reviewUpdateRequest));
    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity<String> reviewDelete(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable(value = "review-id") Long reviewId
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        reviewService.deleteReview(email, reviewId);
        return ResponseEntity.ok("review is deleted");
    }

}
