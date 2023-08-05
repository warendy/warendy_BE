package com.be.friendy.warendy.domain.favorite.controller;


import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.favorite.dto.request.CreateCategory;
import com.be.friendy.warendy.domain.favorite.service.FavoriteService;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class FavoriteController {

    private final TokenProvider tokenProvider;
    private final FavoriteService favoriteService;

    @PostMapping("/add/wines")
    @ResponseStatus(HttpStatus.OK)
    public void addToFavorite(@RequestHeader("Authorization") String authorizationHeader,
                                @RequestBody Long request){
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        favoriteService.addWineToFavorite(email, request);
    }

    @PostMapping("/add/category")
    @ResponseStatus(HttpStatus.OK)
    public void addToCollection(@RequestHeader("Authorization") String authorizationHeader,
                                @RequestBody CreateCategory request){
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        favoriteService.addWineToCategory(email, request);
    }

//    @GetMapping("/wines")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Wine> findAllWinesFromFavorite(@RequestHeader("Authorization") String authorizationHeader){
//        String email = tokenProvider.getEmailFromToken(authorizationHeader);
//        return favoriteService.findAllWines(email);
//    }
}
