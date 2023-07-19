package com.be.friendy.warendy.controller;

import com.be.friendy.warendy.config.TokenProvider;
import com.be.friendy.warendy.domain.dto.request.MemberSignInRequest;
import com.be.friendy.warendy.domain.dto.request.MemberSignUpRequest;
import com.be.friendy.warendy.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberSignUpRequest request){
        var result = this.memberService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody MemberSignInRequest request){
        var member = this.memberService.authenticate(request);
        var token = this.tokenProvider.generateToken(member.getEmail());
        log.info("user login -> " + request.getEmail());
        return ResponseEntity.ok(token);
    }
}
