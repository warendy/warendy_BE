package com.be.friendy.warendy.exception.handler;

import com.be.friendy.warendy.domain.dto.request.MemberSignUpRequest;
import com.be.friendy.warendy.domain.entity.Member;
import com.be.friendy.warendy.domain.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@AllArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 토큰에서 email, oauthType 추출
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        System.out.println(token);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
