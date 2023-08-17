package com.be.friendy.warendy.domain.favorite.controller;


import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.common.ApiResponse;
import com.be.friendy.warendy.domain.favorite.dto.request.CreateCategory;
import com.be.friendy.warendy.domain.favorite.dto.request.GivenWineInfo;
import com.be.friendy.warendy.domain.favorite.dto.response.Collection;
import com.be.friendy.warendy.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class FavoriteController {

    private final TokenProvider tokenProvider;
    private final FavoriteService favoriteService;

    @PostMapping("/add/wines")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> addToFavorite(@RequestHeader("Authorization") String authorizationHeader,
                                     @RequestBody GivenWineInfo request){
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        String result = favoriteService.addWineToFavorite(email, request);
        return ApiResponse.createSuccess(result);
    }

    @PostMapping("/update/category")
    @ResponseStatus(HttpStatus.OK)
    public void updateCollection(@RequestHeader("Authorization") String authorizationHeader,
                                @RequestBody CreateCategory request){
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        favoriteService.updateCategory(email, request);
    }

    @GetMapping("/wines")
    @ResponseStatus(HttpStatus.OK)
    public Collection findAllWinesFromFavorite(@RequestHeader("Authorization") String authorizationHeader){
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return favoriteService.findAllWines(email);
    }

    @DeleteMapping("/delete/wine")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromFavorite(@RequestHeader("Authorization") String authorizationHeader,
                                   @RequestParam(value = "wine-id") Long wineId){
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        favoriteService.deleteFavoriteWine(email, wineId);
    }
}
