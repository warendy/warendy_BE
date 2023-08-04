package com.be.friendy.warendy.domain.favorite.service;

import com.be.friendy.warendy.domain.favorite.dto.request.CreateInfo;
import com.be.friendy.warendy.domain.favorite.entity.FavoriteCategory;
import com.be.friendy.warendy.domain.favorite.repository.FavoriteCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FavoriteCategoryService {

    private final FavoriteCategoryRepository favoriteCategoryRepository;

    public void save(CreateInfo request) {
        System.out.println("list of wine ids= "+request.getWines());
        FavoriteCategory favoriteCategory = new FavoriteCategory().builder()
                .name(request.getName())
                .wineIds(request.getWines())
                .build();
        System.out.println(favoriteCategory);
        favoriteCategoryRepository.save(favoriteCategory);
    }
}
