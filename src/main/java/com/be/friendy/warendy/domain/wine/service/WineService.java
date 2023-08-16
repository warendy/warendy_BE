package com.be.friendy.warendy.domain.wine.service;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.review.repository.ReviewRepository;
import com.be.friendy.warendy.domain.wine.dto.request.Preference;
import com.be.friendy.warendy.domain.wine.dto.response.RecommendWineResponse;
import com.be.friendy.warendy.domain.wine.dto.response.WineDetailSearchResponse;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WineService {

    private final WineRepository wineRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public WineDetailSearchResponse searchWineDetail(Long wineId) {

        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new RuntimeException("the wine does not exist")
        );

        return WineDetailSearchResponse.fromEntity(
                wine.insertReviewList(reviewRepository.findByWine(
                        wine, PageRequest.of(0, 5)).stream().toList()));
    }

    public List<RecommendWineResponse> recommendWine(String Email) {
        Member member = memberRepository.findByEmail(Email).orElseThrow(
                () -> new RuntimeException("the user does not exist"));
        ;
        return wineRepository.findSimilarWines(
                        member.getBody(), member.getDry(),
                        member.getTannin(), member.getAcidity()).stream()
                .map(RecommendWineResponse::fromEntity).toList();
    }

    public List<RecommendWineResponse> recommendWineInLanding(Preference request) {
        return wineRepository.findSimilarWines(
                        request.getBody(), request.getDry(),
                        request.getTannin(), request.getAcidity()).stream()
                .map(RecommendWineResponse::fromEntity).toList();
    }
}
