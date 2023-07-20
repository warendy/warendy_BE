package com.be.friendy.warendy.config.security;


import com.be.friendy.warendy.config.jwt.filter.JwtAuthenticationFilter;
import com.be.friendy.warendy.exception.handler.OAuthLoginFailureHandler;
import com.be.friendy.warendy.exception.handler.OAuthLoginSuccessHandler;
import com.be.friendy.warendy.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    OAuthLoginSuccessHandler oAuthLoginSuccessHandler;

    @Autowired
    OAuthLoginFailureHandler oAuthLoginFailureHandler;

    @Autowired
    MemberService memberService;
    private final JwtAuthenticationFilter authenticationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
        http.csrf().disable();
        http.authorizeHttpRequests(authorize -> {
            try {
                authorize
                        .requestMatchers("/login/**", "/signup/**", "/oauth2/**", "/**")
                        .permitAll() // 해당 경로는 인증 없이 접근 가능
                        .requestMatchers("/member/**") // 해당 경로는 인증이 필요
                        .hasRole("MEMBER") // ROLE 이 MEMBER 가 포함된 경우에만 인증 가능
                    .and()
                        .oauth2Login()
                        .userInfoEndpoint()
                        .userService(memberService)
                    .and()
                        .successHandler(oAuthLoginSuccessHandler)
                        .failureHandler(oAuthLoginFailureHandler)
                    .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
