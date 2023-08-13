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
        int acidity,
        String InBoardIdList
) {

    public static InfoResponse fromEntity(Member member) {
        return InfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .avatar(member.getAvatar())
                .body(member.getBody())
                .dry(member.getDry())
                .tannin(member.getTannin())
                .acidity(member.getAcidity())
                .InBoardIdList(member.getInBoardIdList())
                .build();
    }
}
