package com.be.friendy.warendy.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterInterfaceImpl implements EmitterInterface {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    //Emitter를 저장한다.
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    //이벤트를 저장한다.
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    //해당 회원과 관련된 모든 Emitter를 찾는다.
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //해당 게시물과 관련된 모든 Emitter를 찾는다.
    public Map<String, SseEmitter> findAllEmitterWithByBoardId(String boardId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().contains(boardId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //해당 회원과 관련된 모든 이벤트를 찾는다.
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //Emitter를 지운다.
    public void deleteById(String id) {
        emitters.remove(id);
    }

    //해당 회원과 관련된 모든 Emitter를 지운다.
    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    //해당 회원과 관련된 모든 이벤트를 지운다.
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}
