package com.be.friendy.warendy.domain.notification.service;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.notification.dto.response.NotificationInfo;
import com.be.friendy.warendy.domain.notification.entity.Notification;
import com.be.friendy.warendy.domain.notification.repository.EmitterRepository;
import com.be.friendy.warendy.domain.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class NotificationService {
    private final long timeout = 60L * 1000L * 60L;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public SseEmitter subscribe(Member memberInfo, Long boardId) {
        Long memberId = memberInfo.getId();
        String emitterId = makeTimeIncludeId(memberId, boardId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(memberId, boardId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");
        String bId = String.valueOf(boardId);
        send(bId, memberId, memberInfo.getNickname() +" has joined!");
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        return emitter;
    }

    private String makeTimeIncludeId(Long memberId, Long boardId) {
        return boardId + "_" + memberId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private void send(String boardId, Long joinedMemberId, String content) {
        String eventId = boardId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByBoardId(boardId);
        emitters.forEach(
                (key, emitter) -> {
                    Long memberId = Long.parseLong(key.split("_")[1]);
                    Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
                    if(member.getId() != joinedMemberId) {
                        Notification notification = notificationRepository.save(createNotification(boardId, member, content));
                        emitterRepository.saveEventCache(key, notification);
                        sendNotification(emitter, eventId, key, NotificationInfo.fromEntity(notification));
                    }
                }
        );
    }

    private Notification createNotification(String boardId, Member member, String content) {
        return Notification.builder()
                .receiver(member)
                .boardId(boardId)
                .content(content)
                .isRead(false)
                .build();
    }
}