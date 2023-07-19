package com.be.friendy.warendy.domain.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignInRequest {
    private String email;
    private String password;
}
