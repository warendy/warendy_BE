package com.be.friendy.warendy.domain.favorite.dto.response;

import java.util.List;

public record Category(
        String name,
        List<WineInfo> wines
) {
}
