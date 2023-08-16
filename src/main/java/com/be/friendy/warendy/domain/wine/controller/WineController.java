package com.be.friendy.warendy.domain.wine.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.wine.dto.request.Preference;
import com.be.friendy.warendy.domain.wine.dto.response.RecommendWineResponse;
import com.be.friendy.warendy.domain.wine.dto.response.WineDetailSearchResponse;
import com.be.friendy.warendy.domain.wine.service.WineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WineController {
    private final WineService wineService;
    private final TokenProvider tokenProvider;

    @GetMapping("/wines/{wine-id}/detail")
    public ResponseEntity<WineDetailSearchResponse> wineSearchDetail(
            @PathVariable(value = "wine-id") Long wineId) {

        return ResponseEntity.ok(wineService.searchWineDetail(wineId));
    }

    @GetMapping("/wines/recommendation")
    public ResponseEntity<List<RecommendWineResponse>> wineRecommendation(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(wineService.recommendWine(email));
    }

    @PostMapping("/landing/recommendation")
    public ResponseEntity<List<RecommendWineResponse>> landingRecommendation(
            @RequestBody Preference request) {
        return ResponseEntity.ok(wineService.recommendWineInLanding(request));
    }
}
