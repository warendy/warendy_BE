package com.be.friendy.warendy.domain.wine.repository;

import com.be.friendy.warendy.domain.wine.entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WineRepository extends JpaRepository<Wine, Long> {
    Optional<Wine> findByName(String wineName);

    @Query(value = "SELECT *, "
            + "SQRT(POW(body - ?1, 2) + POW(dry - ?2, 2) + POW(tannin - ?3, 2) + POW(acidity - ?4, 2)) AS similarity "
            + "FROM warendy.wine ORDER BY similarity LIMIT 20", nativeQuery = true)
    List<Wine> findSimilarWines(int body, int dry, int tannin, int acidity);

}

