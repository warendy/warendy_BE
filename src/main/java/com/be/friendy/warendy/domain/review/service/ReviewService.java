package com.be.friendy.warendy.domain.review.service;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.review.dto.request.ReviewUpdateRequest;
import com.be.friendy.warendy.domain.review.dto.request.WineReviewCreateRequest;
import com.be.friendy.warendy.domain.review.dto.response.MyReviewSearchResponse;
import com.be.friendy.warendy.domain.review.dto.response.ReviewUpdateResponse;
import com.be.friendy.warendy.domain.review.dto.response.WineReviewCreateResponse;
import com.be.friendy.warendy.domain.review.dto.response.WineReviewSearchByWineIdResponse;
import com.be.friendy.warendy.domain.review.entity.Review;
import com.be.friendy.warendy.domain.review.repository.ReviewRepository;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final WineRepository wineRepository;

    public WineReviewCreateResponse createWineReview(String email
            , Long wineId, WineReviewCreateRequest wineReviewCreateRequest
    ) {

        Member memberById = memberRepository
                .findByNickname(wineReviewCreateRequest.getNickname())
                .orElseThrow(() -> new RuntimeException("user does not exists"));
        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!memberById.equals(memberByEmail)) {
            throw new RuntimeException("check the user information");
        }

        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new RuntimeException("wine does not exists"));

        return WineReviewCreateResponse.fromEntity(reviewRepository.save(Review
                .builder()
                .member(memberById)
                .wine(wine)
                .contents(wineReviewCreateRequest.getContents())
                .rating(wineReviewCreateRequest.getRating())
                .build()));
    }

    public ReviewUpdateResponse updateReview(
            String email, Long reviewId, ReviewUpdateRequest reviewUpdateRequest
    ) {
        Member memberById = memberRepository
                .findByNickname(reviewUpdateRequest.getNickname())
                .orElseThrow(() -> new RuntimeException("user does not exists"));
        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        if (!memberById.equals(memberByEmail)) {
            throw new RuntimeException("check the user information");
        }

        Review nowReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("the review does not exists"));
        nowReview.updateReviewInfo(reviewUpdateRequest);
        reviewRepository.save(nowReview);
        return ReviewUpdateResponse.fromEntity(nowReview);
    }

    public void deleteReview(String email, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("the review does not exists"));
        Member memberById = memberRepository
                .findByNickname(review.getMember().getNickname())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!memberById.equals(memberByEmail)) {
            throw new RuntimeException("check the user information");
        }

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
            String email, String nickname, Pageable pageable
    ) {
        Member memberById = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!memberById.equals(memberByEmail)) {
            throw new RuntimeException("check the user information");
        }
        return reviewRepository.findByMember(memberById, pageable)
                .map(MyReviewSearchResponse::fromEntity);
    }

}
