package com.be.friendy.warendy.domain.winebar.repository;

import com.be.friendy.warendy.domain.winebar.entity.WineBar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineBarRepository extends JpaRepository<WineBar, Long> {

    Optional<WineBar> findByName(String name);

    List<WineBar> findAllByLatAndLnt(Double lat, Double lnt);

    Optional<WineBar> findByLatAndLnt(Double lat, Double lnt);

}
