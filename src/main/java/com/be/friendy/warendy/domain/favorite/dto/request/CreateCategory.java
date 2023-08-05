package com.be.friendy.warendy.domain.favorite.dto.request;


import lombok.*;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategory {
    private List<CategoryInfo> list;
}

