package com.be.friendy.warendy.domain.review.service;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.review.dto.request.ReviewUpdateRequest;
import com.be.friendy.warendy.domain.review.dto.request.WineReviewCreateRequest;
import com.be.friendy.warendy.domain.review.dto.response.MyReviewSearchResponse;
import com.be.friendy.warendy.domain.review.dto.response.WineReviewCreateResponse;
import com.be.friendy.warendy.domain.review.dto.response.WineReviewSearchByWineIdResponse;
import com.be.friendy.warendy.domain.review.entity.Review;
import com.be.friendy.warendy.domain.review.repository.ReviewRepository;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final WineRepository wineRepository;

    public WineReviewCreateResponse createWineReview(
            Long wineId, WineReviewCreateRequest wineReviewCreateRequest
    ) {

        Member member = memberRepository
                .findByNickname(wineReviewCreateRequest.getNickname())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new RuntimeException("wine does not exists"));

        return WineReviewCreateResponse.fromEntity(reviewRepository.save(Review.builder()
                .member(member)
                .wine(wine)
                .contents(wineReviewCreateRequest.getContents())
                .rating(wineReviewCreateRequest.getRating())
                .build()));
    }

    public Review updateReview(
            Long reviewId, ReviewUpdateRequest reviewUpdateRequest
    ) {
        Review nowReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("the review does not exists"));
        nowReview.updateReviewInfo(reviewUpdateRequest);
        return reviewRepository.save(nowReview);
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("the review does not exists"));
        reviewRepository.save(review);      // - test 코드 검증 위해
        reviewRepository.delete(review);
    }

    public Page<WineReviewSearchByWineIdResponse> searchWineReviewByWineId(
            Long wineId, Pageable pageable
    ) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new RuntimeException("the wine does not exist"));
        return reviewRepository.findByWine(wine, pageable)
                .map(WineReviewSearchByWineIdResponse::fromEntity);
    }

    public Page<MyReviewSearchResponse> searchMyReview(
            String nickname, Pageable pageable
    ) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        return reviewRepository.findByMember(member, pageable)
                .map(MyReviewSearchResponse::fromEntity);
    }

}
