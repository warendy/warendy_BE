package com.be.friendy.warendy.controller;

import com.be.friendy.warendy.config.TokenProvider;
import com.be.friendy.warendy.domain.dto.request.MemberSignInRequest;
import com.be.friendy.warendy.domain.dto.request.MemberSignUpRequest;
import com.be.friendy.warendy.domain.entity.Member;
import com.be.friendy.warendy.service.KakaoUserService;
import com.be.friendy.warendy.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final KakaoUserService kakaoUserService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberSignUpRequest request){
        Member result = this.memberService.signUp(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody MemberSignInRequest request){
        Member member = this.memberService.signIn(request);
        String token = this.tokenProvider.generateToken(member.getEmail());
        log.info("user login -> " + request.getEmail());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/test/oauth2/callback/kakao")
    public MemberSignUpRequest kakaoLogin(@RequestParam String code, HttpServletResponse response)
                                                                    throws JsonProcessingException {
        System.out.println(code);
        return kakaoUserService.kakaoLogin(code, response);
    }
}
