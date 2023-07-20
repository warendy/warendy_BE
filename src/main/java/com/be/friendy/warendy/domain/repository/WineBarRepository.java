package com.be.friendy.warendy.domain.repository;

import com.be.friendy.warendy.domain.entity.WineBar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineBarRepository extends JpaRepository<WineBar, Long> {

    Optional<WineBar> findByName(String name);

}
