package com.be.friendy.warendy.domain.chat.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import com.be.friendy.warendy.domain.chat.entity.Message;
import com.be.friendy.warendy.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final TokenProvider tokenProvider;

    // 채팅방 생성
    @PostMapping("/rooms")
    public ChatRoom createChatRoom(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String name) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return chatService.createChatRoom(email, UUID.randomUUID().toString(), name);
    }

    // 채팅방 삭제
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String roomId, @RequestHeader("Authorization") String authorizationHeader) throws IOException {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        chatService.deleteChatRoom(roomId, email);
        return ResponseEntity.noContent().build();
    }

    // 메시지 전송
    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Void> sendMessage(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId, @RequestParam String content) {
        try {
            String email = tokenProvider.getEmailFromToken(authorizationHeader);
            chatService.sendMessage(roomId, email, content);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 채팅방의 채팅 내역 조회
    @GetMapping("/rooms/{roomId}/messages")
    public List<Message> getChatMessages(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return chatService.getChatMessages(roomId, email);
    }

    // 채팅방 입장
    @PostMapping("/enter/{roomId}")
    public ResponseEntity<Void> enterChatRoom(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId) {
        try {
            String email = tokenProvider.getEmailFromToken(authorizationHeader);
            chatService.enterChatRoom(roomId, email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 채팅방 퇴장
    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<Void> leaveChatRoom(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId) {
        try {
            String email = tokenProvider.getEmailFromToken(authorizationHeader);
            chatService.leaveChatRoom(roomId, email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
