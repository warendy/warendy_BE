package com.be.friendy.warendy.domain.notification.service;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.notification.dto.response.NotificationInfo;
import com.be.friendy.warendy.domain.notification.entity.Notification;
import com.be.friendy.warendy.domain.notification.repository.EmitterInterfaceImpl;
import com.be.friendy.warendy.domain.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class NotificationServices {
    private final long timeout = 60L * 1000L * 60L;
    private final EmitterInterfaceImpl emitterRepository;
    private final NotificationRepository notificationRepository;


    public SseEmitter subscribe(Member memberInfo, Long boardId, String lastEventId) {
        Long memberId = memberInfo.getId();
        String emitterId = makeTimeIncludeId(memberId, boardId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(memberId, boardId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }
        return emitter;
    }

    private String makeTimeIncludeId(Long memberId, Long boardId) {
        return memberId + "_" + boardId + "_" + System.currentTimeMillis();
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

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    public void send(Member receiver, String content) {
        Notification notification = notificationRepository.save(createNotification(receiver, content));

        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationInfo.fromEntity(notification));
                }
        );
    }


    private Notification createNotification(Member receiver, String content) {
        return Notification.builder()
//                .receiver(receiver)
                .content(content)
                .isRead(false)
                .build();
    }
}
