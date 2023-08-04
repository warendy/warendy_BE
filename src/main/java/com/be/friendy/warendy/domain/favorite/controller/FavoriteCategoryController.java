package com.be.friendy.warendy.domain.favorite.controller;


import com.be.friendy.warendy.domain.favorite.dto.request.CreateInfo;
import com.be.friendy.warendy.domain.favorite.service.FavoriteCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteCategoryController {

    private final FavoriteCategoryService favoriteCategoryService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestBody CreateInfo request){
        System.out.println("incomming request= "+request);
        favoriteCategoryService.save(request);
    }
}
