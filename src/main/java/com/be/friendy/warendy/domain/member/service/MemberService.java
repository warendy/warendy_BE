package com.be.friendy.warendy.domain.member.service;

import com.be.friendy.warendy.domain.favorite.dto.request.PasswordCheck;
import com.be.friendy.warendy.domain.member.dto.request.SignInRequest;
import com.be.friendy.warendy.domain.member.dto.request.SignUpRequest;
import com.be.friendy.warendy.domain.member.dto.request.UpdateRequest;
import com.be.friendy.warendy.domain.member.dto.response.InfoResponse;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.exception.member.DuplicatedUserException;
import com.be.friendy.warendy.exception.member.DuplicatedUserNicknameException;
import com.be.friendy.warendy.exception.member.UserDoesNotExistException;
import com.be.friendy.warendy.exception.member.WrongPasswordException;
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
            throw new DuplicatedUserException();
        }
        boolean alreadyInUse = memberRepository.existsByNickname((request.getNickname()));
        if(alreadyInUse){
            throw new DuplicatedUserNicknameException();
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return InfoResponse.fromEntity(memberRepository.save(setAccount(request)));
    }

    public Member signIn(SignInRequest member) {
        Member user = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(UserDoesNotExistException::new);

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        return user;
    }

    public void updateMember(UpdateRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserDoesNotExistException::new);
        boolean alreadyInUse = memberRepository.existsByNickname((request.getNickname()));
        if(alreadyInUse){
            throw new DuplicatedUserNicknameException();
        }
        if(request.getPassword() == null) {
            member.updateMemberInfo(request.getEmail(), request.getPassword(), request.getNickname(), request.getAvatar(),
                    request.getBody(), request.getDry(), request.getTannin(), request.getAcidity());
            memberRepository.save(member);
        }else{
            member.updateMemberInfo(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getNickname(), request.getAvatar(),
                    request.getBody(), request.getDry(), request.getTannin(), request.getAcidity());
            memberRepository.save(member);
        }
    }

    public InfoResponse getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserDoesNotExistException::new);

        return InfoResponse.fromEntity(member);
    }

    public void deleteAccount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(UserDoesNotExistException::new);
        memberRepository.delete(member);
    }

    public Member loadUserByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public void checkPassword(String email,PasswordCheck request){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserDoesNotExistException::new);

        if (!this.passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new WrongPasswordException();
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
}
