package com.be.friendy.warendy.domain.favorite.dto.response;

import java.util.List;

public record Collection(
        List<WineInfo> list,
        List<Category> categoryList
) {
}
