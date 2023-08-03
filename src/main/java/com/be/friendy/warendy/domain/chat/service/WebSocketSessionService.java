package com.be.friendy.warendy.domain.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class WebSocketSessionService {
    private final Map<String, Set<WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();

    public void enterChatRoom(String roomId, WebSocketSession session) {
        chatRoomSessions.computeIfAbsent(roomId, k -> Collections.newSetFromMap(
                new ConcurrentHashMap<WebSocketSession, Boolean>())).add(session);
    }

    // 채팅방 연결 끊기
    public void disconnectChatRoom(String roomId, WebSocketSession session) {
        Set<WebSocketSession> roomSessions = chatRoomSessions.get(roomId);
        if (roomSessions != null) {
            roomSessions.remove(session);
        }
    }

    // 채팅방 삭제
    public void deleteChatRoom(String chatRoomId) throws IOException {
        Set<WebSocketSession> sessions = chatRoomSessions.get(chatRoomId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                session.close();  // 각 세션을 닫습니다.
            }
        }
        chatRoomSessions.remove(chatRoomId);  // 채팅방을 맵에서 제거합니다.
    }

    public void registerSession(String chatRoomId, WebSocketSession session) {
        chatRoomSessions.computeIfAbsent(chatRoomId, k -> new CopyOnWriteArraySet<>()).add(session);
    }

    public void unregisterSession(String chatRoomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = chatRoomSessions.get(chatRoomId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    public Set<WebSocketSession> getSessions(String chatRoomId) {
        return chatRoomSessions.getOrDefault(chatRoomId, Collections.emptySet());
    }

}
