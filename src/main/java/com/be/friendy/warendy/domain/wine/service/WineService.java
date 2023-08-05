package com.be.friendy.warendy.domain.wine.service;

import com.be.friendy.warendy.domain.review.repository.ReviewRepository;
import com.be.friendy.warendy.domain.wine.dto.response.WineDetailSearchResponse;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WineService {

    private final WineRepository wineRepository;
    private final ReviewRepository reviewRepository;

    public WineDetailSearchResponse searchWineDetail(Long wineId) {

        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new RuntimeException("the wine does not exist")
        );

        return WineDetailSearchResponse.fromEntity(
                wine.insertReviewList(reviewRepository.findByWine(
                        wine, PageRequest.of(0, 5)).stream().toList()));
    }
}
