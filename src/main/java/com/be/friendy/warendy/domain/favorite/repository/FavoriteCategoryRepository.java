package com.be.friendy.warendy.domain.favorite.repository;

import com.be.friendy.warendy.domain.favorite.entity.FavoriteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCategoryRepository extends JpaRepository<FavoriteCategory, Long> {

}
