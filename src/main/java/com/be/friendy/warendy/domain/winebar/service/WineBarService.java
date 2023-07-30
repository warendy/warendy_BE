package com.be.friendy.warendy.domain.winebar.service;


import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.be.friendy.warendy.domain.winebar.repository.WinebarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WineBarService {

    WinebarRepository winebarRepository;

    public Winebar searchWinebar(Double lat, Double lnt) {
        return winebarRepository.findByLatAndLnt(lat, lnt)
                .orElseThrow(() -> new RuntimeException("the winebar does not exists"));
    }

}
