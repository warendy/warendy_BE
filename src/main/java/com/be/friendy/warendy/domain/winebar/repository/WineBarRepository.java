package com.be.friendy.warendy.domain.winebar.repository;

import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WineBarRepository extends JpaRepository<Winebar, Long> {

    Optional<Winebar> findByName(String name);

    List<Winebar> findAllByLatAndLnt(Double lat, Double lnt);

    Optional<Winebar> findByLatAndLnt(Double lat, Double lnt);

}
