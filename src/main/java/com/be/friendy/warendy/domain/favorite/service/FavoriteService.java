package com.be.friendy.warendy.domain.favorite.service;

import com.be.friendy.warendy.domain.favorite.dto.request.AddWine;
import com.be.friendy.warendy.domain.favorite.dto.request.CreateCategory;
import com.be.friendy.warendy.domain.favorite.dto.response.Category;
import com.be.friendy.warendy.domain.favorite.dto.response.Collection;
import com.be.friendy.warendy.domain.favorite.dto.response.WineInfo;
import com.be.friendy.warendy.domain.favorite.entity.Favorite;
import com.be.friendy.warendy.domain.favorite.repository.FavoriteRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final WineRepository wineRepository;

    public void addWineToFavorite(String email, AddWine request){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        Wine wineData = wineRepository.findById(request.getWineId()).orElseThrow(() -> new RuntimeException("wine does not exist"));

        favoriteRepository.save(Favorite.builder()
                .member(member)
                .wine(wineData)
                .imgUrl(wineData.getPicture())
                .build());
    }

    public void addWineToCategory(String email, CreateCategory request){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        System.out.println(request.getList().size());

        request.getList().forEach((e) -> {
                String[] wineList = e.getWineIds().split(",");
                for(int i = 0; i < wineList.length; i++){
                    Wine wineData = wineRepository.findById(Long.parseLong(wineList[i]))
                            .orElseThrow(() -> new RuntimeException("wine does not exist"));
                    favoriteRepository.save(Favorite.builder()
                            .member(member)
                            .wine(wineData)
                            .imgUrl(wineData.getPicture())
                            .category(e.getName())
                            .build());
                }});
    }

    public Collection findAllWines(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        Map<String, List<WineInfo>> categoryMap = new HashMap<>();
        List<WineInfo> wineInfoList = new ArrayList<>(); // List category data

        // Separate favorites with and without category
        for (Favorite favorite : favorites) {
            String categoryName = favorite.getCategory();
            WineInfo wineInfo = new WineInfo(favorite.getWine().getId(), favorite.getImgUrl());

            if (categoryName == null) {
                wineInfoList.add(wineInfo); // Add to list category
            } else {
                categoryMap.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(wineInfo);
            }
        }

        List<Category> categoryList = categoryMap.entrySet().stream()
                .map(entry -> new Category(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        return new Collection(wineInfoList, categoryList);
    }

}
