package com.be.friendy.warendy.domain.wine.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
public class Preference {
    private int body;
    private int acidity;
    private int dry;
    private int tannin;
}
