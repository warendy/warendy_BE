package com.be.friendy.warendy.domain.member.dto.response;

import com.be.friendy.warendy.domain.member.dto.request.SignUpRequest;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
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
    private String password;
    private String avatar;

    private Role role;
    private String oauthType;

    private int body;
    private int dry;
    private int tannin;
    private int acidity;



    public static InfoResponse fromEntity(Member member) {
        return InfoResponse.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .oauthType(member.getOauthType())
                .avatar(member.getAvatar())
                .role(member.getRole())
                .body(member.getBody())
                .dry(member.getDry())
                .tannin(member.getTannin())
                .acidity(member.getAcidity())
                .build();
    }
}
