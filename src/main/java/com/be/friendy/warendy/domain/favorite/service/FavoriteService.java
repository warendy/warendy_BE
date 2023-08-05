package com.be.friendy.warendy.domain.favorite.service;

import com.be.friendy.warendy.domain.favorite.dto.request.CreateCategory;
import com.be.friendy.warendy.domain.favorite.entity.Favorite;
import com.be.friendy.warendy.domain.favorite.repository.FavoriteRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final WineRepository wineRepository;

    public void addWineToFavorite(String email, Long request){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        Wine wineData = wineRepository.findById(request).orElseThrow(() -> new RuntimeException("wine does not exist"));

        favoriteRepository.save(Favorite.builder()
                .member(member)
                .wine(wineData)
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
                            .category(e.getName())
                            .build());
                }});
    }

    private final String[] getEachWineId(String e) {
        return e.split(",");
    }
//    public List<Wine> findAllWines(String email){
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("user does not exist"));
//
//        return favoriteRepository.findAllFavoriteWinesWithMemberId(member.getId());
//    }
}
