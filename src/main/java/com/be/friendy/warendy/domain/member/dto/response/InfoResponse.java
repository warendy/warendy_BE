package com.be.friendy.warendy.domain.member.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoResponse {

    private String email;
    private String nickname;
    private String avatar;
    private String mbti;
    private int body;
    private int dry;
    private int tannin;
    private int acidity;

}
