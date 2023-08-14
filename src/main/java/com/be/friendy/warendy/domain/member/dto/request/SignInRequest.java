package com.be.friendy.warendy.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
public class SignInRequest {
    @NotBlank(message = "이메일이 비어있을수 없습니다")
    private String email;
    @NotBlank(message = "비밀번호가 비어있을수 없습니다")
    private String password;
}
