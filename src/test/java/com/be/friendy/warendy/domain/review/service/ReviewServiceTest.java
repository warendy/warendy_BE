package com.be.friendy.warendy.domain.review.service;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private WineRepository wineRepository;

    @Mock
    private TokenProvider tokenProvider;


    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("success create! - wine review")
    void successCreateWineReview() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("Hong")
                .avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Wine wine1 = Wine.builder()
                .id(1L)
                .name("AA")
                .vintage(1234)
                .price("12345")
                .picture("url-?")
                .body(1).dry(1).tannin(1).acidity(1).alcohol(1.1)
                .grapes("grapes")
                .pairing("food")
                .region("region1")
                .type("types")
                .winery("winery")
                .rating(1.1f)
                .build();
        // 순서대로
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        given(wineRepository.findById(anyLong()))
                .willReturn(Optional.of(wine1));
        given(reviewRepository.save(any()))
                .willReturn(Review.builder()
                        .id(1L)
                        .member(member1)
                        .wine(wine1)
                        .contents("content")
                        .rating(4.4f)
                        .build());
        //when
        WineReviewCreateRequest createRequest =
                WineReviewCreateRequest.builder()
                        .nickname("A")
                        .contents("B")
                        .rating(4.3f)
                        .build();
        WineReviewCreateResponse createResponse =
                reviewService.createWineReview("AAA", 15L, createRequest);
        //then
        assertEquals("Hong", createResponse.getNickname());
        assertEquals("AA", createResponse.getWineName());
    }

    @Test
    @DisplayName("success search with wine id! - review")
    void successSearchWineReviewByWineId() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("Hong")
                .avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Wine wine1 = Wine.builder()
                .id(1L)
                .name("AA")
                .vintage(1234)
                .price("12345")
                .picture("url-?")
                .body(1).dry(1).tannin(1).acidity(1).alcohol(1.1)
                .grapes("grapes")
                .pairing("food")
                .region("region1")
                .type("types")
                .winery("winery")
                .rating(1.1f)
                .build();
        Wine wine2 = Wine.builder()
                .id(34L)
                .name("Abbbbbb")
                .vintage(4567)
                .price("123")
                .picture("url")
                .body(2).dry(2).tannin(2).acidity(2).alcohol(2.1)
                .grapes("grapes2222")
                .pairing("food")
                .region("region22222")
                .type("types22222")
                .winery("winery2222")
                .rating(1.1f)
                .build();
        List<Review> responseList =
                Arrays.asList(
                        Review.builder()
                                .id(123L)
                                .member(member1)
                                .wine(wine1)
                                .contents("AAA")
                                .rating(1.1f)
                                .build(),
                        Review.builder()
                                .id(456L)
                                .member(member1)
                                .wine(wine1)
                                .contents("BBB")
                                .rating(2.f)
                                .build(),
                        Review.builder()
                                .id(789L)
                                .member(member1)
                                .wine(wine2)
                                .contents("CCC")
                                .rating(3.1f)
                                .build()
                );
        given(wineRepository.findById(anyLong()))
                .willReturn(Optional.of(wine1));
        given(reviewRepository.findByWine(any(), any()))
                .willReturn(new PageImpl<>(responseList));
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<WineReviewSearchByWineIdResponse> reviewPage =
                reviewService.searchWineReviewByWineId(14L, pageable);
        //then
        assertEquals(3, reviewPage.getTotalElements());
        assertEquals(1, reviewPage.getTotalPages());
    }

    @Test
    @DisplayName("success search with wine id! - review")
    void successSearchMyReview() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("Hong")
                .avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Wine wine1 = Wine.builder()
                .id(1L)
                .name("AA")
                .vintage(1234)
                .price("12345")
                .picture("url-?")
                .body(1).dry(1).tannin(1).acidity(1).alcohol(1.1)
                .grapes("grapes")
                .pairing("food")
                .region("region1")
                .type("types")
                .winery("winery")
                .rating(1.1f)
                .build();
        Wine wine2 = Wine.builder()
                .id(34L)
                .name("Abbbbbb")
                .vintage(4567)
                .price("123")
                .picture("url")
                .body(2).dry(2).tannin(2).acidity(2).alcohol(2.1)
                .grapes("grapes2222")
                .pairing("food")
                .region("region22222")
                .type("types22222")
                .winery("winery2222")
                .rating(1.1f)
                .build();
        List<Review> responseList =
                Arrays.asList(
                        Review.builder()
                                .id(123L)
                                .member(member1)
                                .wine(wine1)
                                .contents("AAA")
                                .rating(1.1f)
                                .build(),
                        Review.builder()
                                .id(456L)
                                .member(member1)
                                .wine(wine1)
                                .contents("BBB")
                                .rating(2.f)
                                .build(),
                        Review.builder()
                                .id(789L)
                                .member(member1)
                                .wine(wine2)
                                .contents("CCC")
                                .rating(3.1f)
                                .build()
                );
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        given(reviewRepository.findByMember(any(), any()))
                .willReturn(new PageImpl<>(responseList));
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<MyReviewSearchResponse> reviewPage =
                reviewService.searchMyReview("AAA", pageable);
        //then
        assertEquals(3, reviewPage.getTotalElements());
        assertEquals(1, reviewPage.getTotalPages());
    }

    @Test
    @DisplayName("success update! - review")
    void successUpdateReview() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("Hong")
                .avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Wine wine1 = Wine.builder()
                .id(1L)
                .name("AA")
                .vintage(1234)
                .price("12345")
                .picture("url-?")
                .body(1).dry(1).tannin(1).acidity(1).alcohol(1.1)
                .grapes("grapes")
                .pairing("food")
                .region("region1")
                .type("types")
                .winery("winery")
                .rating(1.1f)
                .build();
        Review targetReview = Review.builder()
                .id(15L)
                .member(member1)
                .wine(wine1)
                .contents("AAAAAAA")
                .rating(4.2f)
                .build();
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(targetReview));
        given(reviewRepository.save(any())).willReturn(targetReview);
        //when
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .nickname("AAA")
                .contents("CC")
                .rating(2.3f)
                .build();
        ReviewUpdateResponse review = reviewService.updateReview("AAA", 34L, request);
        //then
        assertEquals("Hong", review.getNickname());
        assertEquals("CC", review.getContents());
        assertEquals(2.3f, review.getRating());
    }

    @Test
    @DisplayName("success delete! - review")
    void successDeleteReview() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("Hong")
                .avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Wine wine1 = Wine.builder()
                .id(1L)
                .name("AA")
                .vintage(1234)
                .price("12345")
                .picture("url-?")
                .body(1).dry(1).tannin(1).acidity(1).alcohol(1.1)
                .grapes("grapes")
                .pairing("food")
                .region("region1")
                .type("types")
                .winery("winery")
                .rating(1.1f)
                .build();
        Review targetReview = Review.builder()
                .id(15L)
                .member(member1)
                .wine(wine1)
                .contents("AAAAAAA")
                .rating(4.2f)
                .build();
        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(targetReview));
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        //when
        reviewService.deleteReview("AAA", 143L);
        //then
        verify(reviewRepository, times(1)).save(captor.capture());
        assertEquals(15L, captor.getValue().getId());
    }

}