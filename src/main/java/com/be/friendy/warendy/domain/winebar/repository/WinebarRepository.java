package com.be.friendy.warendy.domain.winebar.repository;

import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WinebarRepository extends JpaRepository<Winebar, Long> {

    Optional<Winebar> findByName(String name);

    Optional<Winebar> findByLatAndLnt(Double lat, Double lnt);

    String distance = "ROUND((6371 * acos(cos(radians(3)) * cos(radians(lat)) " +
            "* cos(radians(lnt) - radians(3)) + sin(radians(3)) " +
            "* sin(radians(lat))))) AS distance ";
    @Query(value = "SELECT *, " + distance
            + "FROM warendy.winebar ORDER BY distance LIMIT 20", nativeQuery = true)
    List<Winebar> findByDistance(Double lat, Double lnt);

}