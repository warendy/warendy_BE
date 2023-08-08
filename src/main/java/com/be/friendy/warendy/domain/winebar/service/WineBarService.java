package com.be.friendy.warendy.domain.winebar.service;


import com.be.friendy.warendy.domain.winebar.dto.response.WinebarSearchResponse;
import com.be.friendy.warendy.domain.winebar.repository.WinebarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WineBarService {

    private final WinebarRepository winebarRepository;

    public WinebarSearchResponse searchWinebar(Double lat, Double lnt) {
        return WinebarSearchResponse.fromEntity(
                winebarRepository.findByLatAndLnt(lat, lnt).orElseThrow(
                        () -> new RuntimeException("the winebar does not exists")));

    }

    public List<WinebarSearchResponse> searchWinebarAround(
            Double lat, Double lnt
    ) {

        return winebarRepository.findByDistance(lat, lnt).stream()
                .map(WinebarSearchResponse::fromEntity).toList();
    }
}
