package com.be.friendy.warendy.domain.wine.service;

import com.be.friendy.warendy.domain.review.entity.Review;
import com.be.friendy.warendy.domain.review.repository.ReviewRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WineServiceTest {
    @Mock
    private WineRepository wineRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private WineService wineService;

    @Test
    void successSearchWineDetail() {
        //given
        List<String> food = new ArrayList<>();
        food.add("apple");
        food.add("banana");
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
                .paring(food)
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
        given(wineRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> wineService.searchWineDetail(1L));
        //then
        assertEquals("the wine does not exist", exception.getMessage());
    }
}