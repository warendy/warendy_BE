package com.be.friendy.warendy.domain.winebar.repository;

import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WinebarRepository extends JpaRepository<Winebar, Long> {

    Optional<Winebar> findByName(String name);

    Optional<Winebar> findByLatAndLnt(Double lat, Double lnt);

}
