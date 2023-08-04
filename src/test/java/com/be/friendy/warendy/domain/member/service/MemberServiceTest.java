package com.be.friendy.warendy.domain.member.service;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.member.dto.request.SignInRequest;
import com.be.friendy.warendy.domain.member.dto.request.SignUpRequest;
import com.be.friendy.warendy.domain.member.dto.request.UpdateRequest;
import com.be.friendy.warendy.domain.member.dto.response.InfoResponse;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void successCreateMember(){
        //given
        Member newMember = Member.builder()
                .email("test123@test.com")
                .password("test123")
                .nickname("testNick")
                .role(Role.MEMBER)
                .oauthType("kakao")
                .body(1)
                .dry(2)
                .acidity(3)
                .tannin(4)
                .build();
        memberRepository.save(newMember);

        given(memberRepository.findByEmail(newMember.getEmail()))
                .willReturn(Optional.of(newMember));
        //when
        Optional<Member> member = memberRepository.findByEmail(newMember.getEmail());
        //then
        assertEquals(1, member.get().getBody());
        assertEquals(2, member.get().getDry());
        assertEquals(3, member.get().getAcidity());
        assertEquals(4, member.get().getTannin());
        assertEquals("testNick", member.get().getNickname());
        assertEquals("kakao", member.get().getOauthType());
        assertEquals(Role.MEMBER, member.get().getRole());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void successDeleteMember(){
        //given
        Member newMember = Member.builder()
                .id(1L)
                .email("test123@test.com")
                .password("test123")
                .nickname("testNick")
                .role(Role.MEMBER)
                .oauthType("kakao")
                .body(1)
                .dry(2)
                .acidity(3)
                .tannin(4)
                .build();
        memberRepository.save(newMember);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(newMember));
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        //when
        memberService.deleteAccount(1L);
        //then
        verify(memberRepository, times(1)).save(captor.capture());
        assertEquals(1, captor.getValue().getId());
    }

    @Test
    @DisplayName("회원 수정")
    void successUpdateMember(){
        //given
        Member newMember = Member.builder()
                .id(1L)
                .email("test123@test.com")
                .password("test123")
                .nickname("testNick")
                .role(Role.MEMBER)
                .oauthType("kakao")
                .body(1)
                .dry(2)
                .acidity(3)
                .tannin(4)
                .build();
        memberRepository.save(newMember);
        given(memberRepository.save(any())).willReturn(newMember);
        given(memberRepository.findByEmail(newMember.getEmail()))
                .willReturn(Optional.of(newMember));
        //when
        UpdateRequest request = UpdateRequest.builder()
                .nickname("updatedNick")
                .body(4)
                .dry(3)
                .acidity(2)
                .tannin(1)
                .build();
        memberService.updateMember(request, newMember.getEmail());

        Optional<Member> member = memberRepository.findByEmail(newMember.getEmail());
        //then
        assertEquals(4, member.get().getBody());
        assertEquals(3, member.get().getDry());
        assertEquals(2, member.get().getAcidity());
        assertEquals(1, member.get().getTannin());
        assertEquals("updatedNick", member.get().getNickname());
    }

    @Test
    @DisplayName("회원 로그인")
    void successLoginMember(){
        //given
        Member newMember = Member.builder()
                .id(1L)
                .email("test123@test.com")
                .password(passwordEncoder.encode("test123"))
                .nickname("testNick")
                .role(Role.MEMBER)
                .oauthType("kakao")
                .body(1)
                .dry(2)
                .acidity(3)
                .tannin(4)
                .build();
        memberRepository.save(newMember);
        SignInRequest request = SignInRequest.builder()
                .email("test123@test.com")
                .password("test123")
                .build();
        given(memberRepository.findByEmail(newMember.getEmail()))
                .willReturn(Optional.of(newMember));
        given(passwordEncoder.matches(request.getPassword(), newMember.getPassword())).willReturn(true);
        //when
        Member user = memberService.signIn(request);
        //then
        assertEquals(user.getNickname(), newMember.getNickname());
        assertEquals(user.getBody(), newMember.getBody());
        assertEquals(user.getDry(), newMember.getDry());
        assertEquals(user.getEmail(), newMember.getEmail());
    }

}