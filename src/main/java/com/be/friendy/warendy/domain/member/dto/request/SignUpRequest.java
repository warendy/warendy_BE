package com.be.friendy.warendy.domain.member.dto.request;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String email;
    private String password;
    private String nickname;
    private String avatar;

    private Role role;
    private String oauthType;

    private int body;
    private int dry;
    private int tannin;
    private int acidity;


}
