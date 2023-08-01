package com.be.friendy.warendy.domain.wine.repository;

import com.be.friendy.warendy.domain.wine.entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WineRepository extends JpaRepository<Wine, Long> {

}
