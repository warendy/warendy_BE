package com.be.friendy.warendy.domain.member.dto.response;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import lombok.*;

@Builder
public record InfoResponse (
        String email,
        String nickname,
        String password,
        String avatar,
        Role role,
        String oauthType,
        int body,
        int dry,
        int tannin,
        int acidity
) {

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
