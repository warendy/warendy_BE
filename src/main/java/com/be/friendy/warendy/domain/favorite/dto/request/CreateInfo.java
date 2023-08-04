package com.be.friendy.warendy.domain.favorite.dto.request;


import lombok.*;

import java.util.Map;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInfo {
    private String name;
    private Map<String, String> wines;
}
