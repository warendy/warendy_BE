package com.be.friendy.warendy.domain.member.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequest {

    private String email;
    private String password;
    private String nickname;
    private String avatar;
    private int body;
    private int dry;
    private int tannin;
    private int acidity;
}
