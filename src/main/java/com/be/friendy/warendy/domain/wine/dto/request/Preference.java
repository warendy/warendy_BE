package com.be.friendy.warendy.domain.wine.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
public class Preference {
    @NonNull
    private int body;
    @NonNull
    private int acidity;
    @NonNull
    private int dry;
    @NonNull
    private int tannin;
}
