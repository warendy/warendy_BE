package com.be.friendy.warendy.domain.member.service;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.favorite.dto.request.PasswordCheck;
import com.be.friendy.warendy.domain.member.dto.request.SignInRequest;
import com.be.friendy.warendy.domain.member.dto.request.SignUpRequest;
import com.be.friendy.warendy.domain.member.dto.request.UpdateRequest;
import com.be.friendy.warendy.domain.member.dto.response.InfoResponse;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public InfoResponse signUp(SignUpRequest request) {
        //이미 들록된 유저인지 확인
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if (exists) {
            throw new RuntimeException("already exists");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return InfoResponse.fromEntity(memberRepository.save(setAccount(request)));
    }

    public Member signIn(SignInRequest member) {
        Member user = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("wrong password");
        }
        return user;
    }

    public void updateMember(UpdateRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        member.updateMemberInfo(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getNickname(), request.getAvatar(),
                request.getBody(), request.getDry(), request.getTannin(), request.getAcidity());
        memberRepository.save(member);
    }

    public InfoResponse getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        return InfoResponse.fromEntity(member);
    }

    public void deleteAccount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("user does not exist"));
        memberRepository.delete(member);
    }

    public Member loadUserByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("already exist"));
    }

    public void checkPassword(String email,PasswordCheck request){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!this.passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("wrong password");
        }
    }

    private Member setAccount(SignUpRequest request) {
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

    private void sendTokenToHeader(String token, HttpServletResponse response){
        response.addHeader("Authorization", "BEARER" + " " + token);
    }
}
