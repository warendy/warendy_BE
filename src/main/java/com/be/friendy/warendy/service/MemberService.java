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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("already exists"));
    }

    public Member signUp(MemberSignUpRequest member) {
        boolean exists = this.memberRepository.existsByEmail(member.getEmail());
        if(exists) {
            throw new RuntimeException("already exists");
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());

        return result;
    }
    public Member signIn(MemberSignInRequest member) {
        var user = this.memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("wrong password");
        }

        return user;
    }
}
