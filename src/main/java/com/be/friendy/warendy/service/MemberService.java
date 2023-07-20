package com.be.friendy.warendy.service;

import com.be.friendy.warendy.domain.dto.request.MemberSignInRequest;
import com.be.friendy.warendy.domain.dto.request.MemberSignUpRequest;
import com.be.friendy.warendy.domain.entity.Member;
import com.be.friendy.warendy.domain.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //email, oauthType 호출
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        String email = null;
        String oauthType = userRequest.getClientRegistration().getRegistrationId();

        // oauth 타입에 따라 데이터가 다르기에 분기
        if("kakao".equals(oauthType.toLowerCase())) {
            email = ((Map<String, Object>)attributes.get("kakao_account")).get("email").toString();
        } else if ("naver".equals(oauthType.toLowerCase())) {
            email = ((Map<String, Object>)attributes.get("response")).get("email").toString();
        }

        // User 존재여부 확인 및 없으면 생성
        if(getUserByEmailAndOAuthType(email, oauthType) == null){
            MemberSignUpRequest member = new MemberSignUpRequest();
            member.setEmail(email);
            member.setOauthType(oauthType);
            this.memberRepository.save(member.toEntity());
        }
        return super.loadUser(userRequest);
    }

    private Member getUserByEmailAndOAuthType(String email, String oauthType) {
        return memberRepository.findByEmailAndOauthType(email, oauthType).orElse(null);
    }


    public Member signUp(MemberSignUpRequest member) {
        boolean exists = this.memberRepository.existsByEmail(member.getEmail());
        if(exists) {
            throw new RuntimeException("already exists");
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        Member result = this.memberRepository.save(member.toEntity());

        return result;
    }
    public Member signIn(MemberSignInRequest member) {
        Member user = this.memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("wrong password");
        }

        return user;
    }
    public UserDetails loadUserByUsername(String email) {
        return this.memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("already exists"));
    }
}
