package com.be.friendy.warendy.domain.member.service;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.member.dto.request.SignUpRequest;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    public SignUpRequest kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        SignUpRequest kakaoUserInfoDto = getKakaoUserInfo(accessToken);

        // 3. 카카오ID로 회원가입 처리
        Member kakaoUser = signupKakaoUser(kakaoUserInfoDto);

        // 4. 강제 로그인 처리
        Authentication authentication = forceLoginKakaoUser(kakaoUser);

        // 5. response Header에 JWT 토큰 추가
        kakaoUsersAuthorizationInput(authentication, response);
        return kakaoUserInfoDto;
    }

    private SignUpRequest getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("access token:" + accessToken);
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = String.valueOf(jsonNode.get("id"));
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        SignUpRequest member = SignUpRequest.builder()
                .email(id)
                .nickname(nickname)
                .build();
        return member;

    }

    private Member signupKakaoUser(SignUpRequest kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String sId = kakaoUserInfo.getEmail();
        String email = sId + "@warendy.com";
        Member user = memberRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // 회원가입
            String nickName = kakaoUserInfo.getNickname();
            String profileImgUrl = kakaoUserInfo.getAvatar();
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            SignUpRequest socialUser = SignUpRequest.builder()
                    .email(email)
                    .nickname(nickName)
                    .oauthType("kakao")
                    .avatar(profileImgUrl)
                    .password(encodedPassword)
                    .build();
            user = memberRepository.save(socialUser.toEntity());

            return user;

        }
        return user;
    }
    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", this.clientId);
        body.add("redirect_uri", this.redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private Authentication forceLoginKakaoUser(Member kakaoUser) {
        log.info("userinfo" +kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoUser, null, kakaoUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
    private void kakaoUsersAuthorizationInput(Authentication authentication, HttpServletResponse response) {
        // response header에 token 추가
        Member userDetailsImpl = ((Member) authentication.getPrincipal());
        String token = tokenProvider.generateToken(userDetailsImpl.getEmail());
        response.addHeader("Authorization", "BEARER" + " " + token);
    }
}