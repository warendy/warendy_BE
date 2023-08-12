package com.be.friendy.warendy.domain.favorite.dto.response;

import com.be.friendy.warendy.domain.wine.entity.Wine;

public record WineInfo(
        Long wine_id,
        String wine_name,
        String wine_img

) {
}
