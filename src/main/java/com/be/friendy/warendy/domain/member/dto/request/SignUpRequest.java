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
    private String mbti;

    private Role role;
    private String oauthType;
    private String socialId;

    private int body;
    private int dry;
    private int tannin;
    private int acidity;

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .socialId(this.socialId)
                .oauthType(this.oauthType)
                .avatar(null)
                .role(Role.USER)
                .mbti(this.mbti)
                .body(this.body)
                .dry(this.dry)
                .tannin(this.tannin)
                .acidity(this.acidity)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
