package com.be.friendy.warendy.domain.favorite.repository;

import com.be.friendy.warendy.domain.favorite.entity.Favorite;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByMember(Member member);

     Optional<Favorite> findByWineId(Long wineId);

     Favorite findByWineIdAndMember(Long windId, Member member);
}
