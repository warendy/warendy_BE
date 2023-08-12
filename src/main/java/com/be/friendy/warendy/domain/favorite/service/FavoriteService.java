package com.be.friendy.warendy.domain.favorite.service;

import com.be.friendy.warendy.domain.favorite.dto.request.GivenWineInfo;
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

    public void addWineToFavorite(String email, GivenWineInfo givenWineInfo){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        Wine wineData = wineRepository.findById(givenWineInfo.getWineId()).orElseThrow(() -> new RuntimeException("wine does not exist"));

        favoriteRepository.save(Favorite.builder()
                .member(member)
                .wine(wineData)
                .wineName(wineData.getName())
                .picture(wineData.getPicture())
                .build());
    }

    public void updateCategory(String email, CreateCategory request){
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        request.getList().forEach((e) -> {
                String[] wineList = e.getWineIds().split(",");
                for(int i = 0; i < wineList.length; i++){
                    Favorite data = favoriteRepository.findByWineId(Long.parseLong(wineList[i]))
                            .orElseThrow(() -> new RuntimeException("wine does not exist in your collection"));
                    Favorite updatedFavorite = updateCategory(data, e.getName());
                    favoriteRepository.save(updatedFavorite);
                }});
    }

    public void deleteFavoriteWine(String email, GivenWineInfo givenWineInfo){
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        Favorite favorite = favoriteRepository.findByWineId(givenWineInfo.getWineId())
                .orElseThrow(() -> new RuntimeException("wine does not exist in your collection"));
        favoriteRepository.delete(favorite);
    }

    public Collection findAllWines(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        Map<String, List<WineInfo>> categoryMap = new HashMap<>();
        List<WineInfo> wineInfoList = new ArrayList<>();

        for (Favorite favorite : favorites) {
            String categoryName = favorite.getCategory();
            WineInfo wineInfo = new WineInfo(favorite.getWine().getId(), favorite.getWine().getName(), favorite.getPicture());
            if (categoryName == null) {
                wineInfoList.add(wineInfo);
            } else {
                categoryMap.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(wineInfo);
            }
        }
        List<Category> categoryList = categoryMap.entrySet().stream()
                .map(entry -> new Category(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new Collection(wineInfoList, categoryList);
    }

    private Favorite updateCategory(Favorite data, String element){
        if(element == null){
            return  Favorite.builder()
                    .id(data.getId())
                    .category(null)
                    .wineName(data.getWineName())
                    .member(data.getMember())
                    .picture(data.getPicture())
                    .wine(data.getWine())
                    .build();
        }
        return  Favorite.builder()
                            .id(data.getId())
                            .category(element)
                            .wineName(data.getWineName())
                            .member(data.getMember())
                            .picture(data.getPicture())
                            .wine(data.getWine())
                        .build();
    }

}
