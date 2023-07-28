package com.be.friendy.warendy.domain.notification.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID",
                                    required = false, defaultValue = "") String lastEventId,
                                @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);
        String email = tokenProvider.getEmail(jwtToken);
        Member memberInfo = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user does not exists"));
        return notificationService.subscribe(memberInfo.getId(), lastEventId);
    }


}
