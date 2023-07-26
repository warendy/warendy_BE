package com.be.friendy.warendy.domain.winebar.service;


import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.be.friendy.warendy.domain.winebar.repository.WineBarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WineBarService {

    WineBarRepository wineBarRepository;

    public List<Winebar> searchWineBar(Double lat, Double lnt) {
        return wineBarRepository.findAllByLatAndLnt(lat, lnt);
    }

}
