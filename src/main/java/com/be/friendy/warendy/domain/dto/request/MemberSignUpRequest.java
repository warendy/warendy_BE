package com.be.friendy.warendy.domain.dto.request;

import com.be.friendy.warendy.domain.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignUpRequest {

    private String email;

    private String password;

    private String nickname;

    private String avatar;

    private String mbti;

    private String role;

    private int body;

    private int dry;

    private int tannin;

    private int acidity;

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .avatar(null)
                .role("ROLE_MEMBER")
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
