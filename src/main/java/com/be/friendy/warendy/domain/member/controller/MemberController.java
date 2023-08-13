package com.be.friendy.warendy.domain.member.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.common.ApiResponse;
import com.be.friendy.warendy.domain.favorite.dto.request.PasswordCheck;
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
import org.springframework.http.HttpStatus;
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
    public ApiResponse<InfoResponse> signUp(@RequestBody SignUpRequest request){
        InfoResponse result = memberService.signUp(request);
        return ApiResponse.createSuccess(result);
    }

    @PostMapping("/signin")
    public ApiResponse<InfoResponse> signIn(@RequestBody SignInRequest request, HttpServletResponse response){
        Member user = memberService.signIn(request);
        String token = tokenProvider.generateToken(user.getEmail());
        response.addHeader("Authorization", "Bearer" + " " + token);
        log.info("user login -> " + request.getEmail());
        return ApiResponse.createSuccess(InfoResponse.fromEntity(user));
    }

    @GetMapping("/members")
    public ApiResponse<InfoResponse> getInfo(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.substring(7);
        String email = tokenProvider.getEmail(jwtToken);
        InfoResponse result = memberService.getMemberInfo(email);
        return ApiResponse.createSuccess(result);
    }

    @PatchMapping("/members")
    public ApiResponse<?> updateAccount(@RequestHeader("Authorization") String authorizationHeader,
                                           @RequestBody UpdateRequest request){
        String jwtToken = authorizationHeader.substring(7);
        String email = tokenProvider.getEmail(jwtToken);
        memberService.updateMember(request, email);
        return ApiResponse.createSuccess("수정완료!");
    }

    @DeleteMapping("/members/{memberId}")
    public ApiResponse<?> deleteAccount(@PathVariable Long memberId) {
        memberService.deleteAccount(memberId);
        return ApiResponse.createSuccess("삭제완료!");
    }

    @GetMapping("/oauth2/callback/kakao")
    public InfoResponse kakaoLogin(@RequestParam String code, HttpServletResponse response)
            throws JsonProcessingException {
        System.out.println(code);
        return kakaoUserService.kakaoLogin(code, response);
    }

    @PostMapping("/members/check")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> passwordCheckBeforeUpdate(@RequestHeader("Authorization") String authorizationHeader,
                                          @RequestBody PasswordCheck request){
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        memberService.checkPassword(email, request);
        return ApiResponse.createSuccess("비밀번호가 일치합니다");
    }
}

