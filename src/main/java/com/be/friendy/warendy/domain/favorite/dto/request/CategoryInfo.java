package com.be.friendy.warendy.domain.favorite.dto.request;


import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryInfo {
    private String name;
    private String wineIds;

}
