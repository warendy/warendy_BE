package com.be.friendy.warendy.domain.member.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequest {
    private String email;
    private String password;
}
