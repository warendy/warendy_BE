package com.be.friendy.warendy.domain.member.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.member.dto.request.SignInRequest;
import com.be.friendy.warendy.domain.member.dto.request.SignUpRequest;
import com.be.friendy.warendy.domain.member.dto.request.UpdateRequest;
import com.be.friendy.warendy.domain.member.dto.response.InfoResponse;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.service.KakaoUserService;
import com.be.friendy.warendy.domain.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
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
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request){
        Member result = memberService.signUp(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request){
        Member member = memberService.signIn(request);
        String token = tokenProvider.generateToken(member.getEmail());
        log.info("user login -> " + request.getEmail());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/members")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.substring(7);
        String email = tokenProvider.getEmail(jwtToken);
        InfoResponse result = memberService.getMemberInfo(email);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/members")
    public ResponseEntity<?> updateAccount(@RequestHeader("Authorization") String authorizationHeader,
                                           @RequestBody UpdateRequest request){
        String jwtToken = authorizationHeader.substring(7);
        String email = tokenProvider.getEmail(jwtToken);
        memberService.updateMember(request, email);
        return ResponseEntity.ok("updated");
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long memberId) {
        memberService.deleteAccount(memberId);
        return ResponseEntity.ok("삭제 성공");
    }

    @GetMapping("/test/oauth2/callback/kakao")
    public SignUpRequest kakaoLogin(@RequestParam String code, HttpServletResponse response)
                                                                    throws JsonProcessingException {
        System.out.println(code);
        return kakaoUserService.kakaoLogin(code, response);
    }
}
