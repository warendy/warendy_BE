package com.be.friendy.warendy.domain.wine.controller;

import com.be.friendy.warendy.domain.wine.dto.response.WineDetailSearchResponse;
import com.be.friendy.warendy.domain.wine.service.WineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WineController {
    private final WineService wineService;

    @GetMapping("/wines/{wine-id}/detail")
    public ResponseEntity<WineDetailSearchResponse> wineSearchDetail(
            @PathVariable(value = "wine-id") Long wineId) {

        return ResponseEntity.ok(wineService.searchWineDetail(wineId));
    }
}
