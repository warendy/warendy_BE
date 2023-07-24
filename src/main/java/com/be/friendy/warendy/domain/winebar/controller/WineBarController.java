package com.be.friendy.warendy.domain.winebar.controller;

import com.be.friendy.warendy.domain.winebar.service.WineBarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class WineBarController {

    private final WineBarService wineBarService;

    @GetMapping("/winebars")
    public ResponseEntity<List> wineBarSearch (
            @RequestParam Double lat, @RequestParam Double lnt
    ) {
        return ResponseEntity.ok(wineBarService.searchWineBar(lat, lnt));
    }

}
