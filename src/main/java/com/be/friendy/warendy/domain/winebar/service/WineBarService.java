package com.be.friendy.warendy.domain.winebar.service;


import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.be.friendy.warendy.domain.winebar.repository.WinebarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WineBarService {

    WinebarRepository winebarRepository;

    public List<Winebar> searchWinebar(Double lat, Double lnt) {
        return winebarRepository.findAllByLatAndLnt(lat, lnt);
    }

}
