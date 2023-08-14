package com.be.friendy.warendy.domain.member.dto.request;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank(message = "이메일값이 비어있을수 없습니다")
    private String email;
    @NotBlank(message = "비밀번호값이 비어있을수 없습니다")
    private String password;
    @NotBlank(message = "닉네임값이 비어있을수 없습니다")
    private String nickname;
    private String avatar;

    private Role role;
    private String oauthType;

    private int body;
    private int dry;
    private int tannin;
    private int acidity;
}
