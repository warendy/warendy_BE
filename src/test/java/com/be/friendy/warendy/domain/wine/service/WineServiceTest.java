package com.be.friendy.warendy.domain.wine.service;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.review.entity.Review;
import com.be.friendy.warendy.domain.review.repository.ReviewRepository;
import com.be.friendy.warendy.domain.wine.dto.response.RecommendWineResponse;
import com.be.friendy.warendy.domain.wine.dto.response.WineDetailSearchResponse;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WineServiceTest {
    @Mock
    private WineRepository wineRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private WineService wineService;

    @Test
    void successSearchWineDetail() {
        //given
        List<Review> reviewList = new ArrayList<>();
        Wine wine1 = Wine.builder()
                .id(1L)
                .name("wineName")
                .vintage(2014)
                .price("W 25,000")
                .picture("url")
                .body(1)
                .dry(1)
                .tannin(1)
                .acidity(1)
                .alcohol(1.1)
                .grapes("grapes")
                .pairing("food")
                .region("region")
                .type("wineStyle")
                .winery("winery")
                .rating(1.1f)
                .reviewList(reviewList)
                .build();
        given(wineRepository.findById(anyLong())).willReturn(Optional.of(wine1));
        given(reviewRepository.findByWine(any(), any()))
                .willReturn(new PageImpl<>(reviewList));
        //when
        WineDetailSearchResponse wineDetail
                = wineService.searchWineDetail(2L);
        //then
        assertEquals(1, wineDetail.getAcidity());


    }

    @Test
    void failedSearchWineDetailWineNotFound() {
        //given
        given(wineRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> wineService.searchWineDetail(1L));
        //then
        assertEquals("the wine does not exist", exception.getMessage());
    }

    @Test
    void successRecommendWine() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        List<Review> reviewList = new ArrayList<>();
        Wine wine1 = Wine.builder()
                .id(1L)
                .name("wineName")
                .vintage(2014)
                .price("W 25,000")
                .picture("url")
                .body(1)
                .dry(1)
                .tannin(1)
                .acidity(1)
                .alcohol(1.1)
                .grapes("grapes")
                .pairing("food")
                .region("region")
                .type("wineStyle")
                .winery("winery")
                .rating(1.1f)
                .reviewList(reviewList)
                .build();
        List<Wine> wineList = new ArrayList<>();
        wineList.add(wine1);
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        given(wineRepository.findSimilarWines(
                anyInt(), anyInt(), anyInt(), anyInt()))
                .willReturn(wineList);
        //when
        List<RecommendWineResponse> recommendWines
                = wineService.recommendWine(member1.getEmail());
        //then
        assertEquals(1, recommendWines.get(0).getBody());
        assertEquals(wine1.getName(), recommendWines.get(0).getWineName());

    }

    @Test
    void failedRecommendationWineNotFoundMember() {
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> wineService.recommendWine("AAA"));
        //then
        assertEquals("the user does not exist", exception.getMessage());
    }
}