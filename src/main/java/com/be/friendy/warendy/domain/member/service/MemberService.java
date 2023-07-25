package com.be.friendy.warendy.domain.member.service;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.member.dto.request.SignInRequest;
import com.be.friendy.warendy.domain.member.dto.request.SignUpRequest;
import com.be.friendy.warendy.domain.member.dto.request.UpdateRequest;
import com.be.friendy.warendy.domain.member.dto.response.InfoResponse;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@AllArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public InfoResponse signUp(SignUpRequest request) {
        //이미 들록된 유저인지 확인
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if(exists) {
            throw new RuntimeException("already exists");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return InfoResponse.fromEntity(memberRepository.save(setAccount(request)));
    }

    public Member signIn(SignInRequest member) {
        Member user = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("wrong password");
        }

        return user;
    }

    public void updateMember(UpdateRequest request, String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exists"));
        member.updateMemberInfo(request.getEmail(), request.getPassword(), request.getNickname(), request.getAvatar(),
                request.getMbti(), request.getBody(), request.getDry(), request.getTannin(), request.getAcidity());
        memberRepository.save(member);
    }

    public InfoResponse getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        return InfoResponse.fromEntity(member);
    }

    public void deleteAccount(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        memberRepository.delete(member);
    }

    public Member loadUserByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("already exists"));
    }

    private Member setAccount(SignUpRequest request){
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .avatar(request.getAvatar())
                .role(Role.MEMBER)
                .body(request.getBody())
                .dry(request.getDry())
                .tannin(request.getTannin())
                .acidity(request.getAcidity())
                .build();
    }
}
