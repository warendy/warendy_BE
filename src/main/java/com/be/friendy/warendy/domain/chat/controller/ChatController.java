package com.be.friendy.warendy.domain.chat.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.chat.dto.ChatRoomDto;
import com.be.friendy.warendy.domain.chat.dto.MessageDto;
import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import com.be.friendy.warendy.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final TokenProvider tokenProvider;

    // 채팅방 생성
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String name) {
        String email;
        try {
            email = tokenProvider.getEmailFromToken(authorizationHeader);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header", e);
        }
        try {
            return ResponseEntity.ok(chatService.createChatRoom(email, UUID.randomUUID().toString(), name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create chat room", e);
        }
    }

    // 채팅방 정보 조회
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoomDto> getChatRoomInfo(@PathVariable String roomId) {
        try {
            return ResponseEntity.ok(chatService.getChatRoomInfo(roomId));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not get chatroom info", e);
        }
    }

    // 사용자 채팅방 정보 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> getMemberChatRoomInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String email;
        try {
            email = tokenProvider.getEmailFromToken(authorizationHeader);
            return ResponseEntity.ok(chatService.getMemberChatRoomInfo(email));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not get chatroom info", e);
        }
    }

    // 채팅방 삭제
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String roomId, @RequestHeader("Authorization") String authorizationHeader) {
        String email;
        try {
            email = tokenProvider.getEmailFromToken(authorizationHeader);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header", e);
        }
        try {
            chatService.deleteChatRoom(roomId, email);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete chat room", e);
        }
    }

    // 메시지 전송
    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Void> sendMessage(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId, @RequestParam String content) {
        String email;
        try {
            email = tokenProvider.getEmailFromToken(authorizationHeader);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header", e);
        }
        try {
            chatService.sendMessage(roomId, email, content);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not send message", e);
        }
    }

    // 채팅방의 채팅 내역 조회
    @GetMapping("/rooms/{roomId}/messages")
    public List<MessageDto> getChatMessages(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId) {
        String email;
        try {
            email = tokenProvider.getEmailFromToken(authorizationHeader);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header", e);
        }
        try {
            return chatService.getChatMessages(roomId, email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not get chat messages", e);
        }
    }

    // 채팅방 입장
    @PostMapping("/enter/{roomId}")
    public ResponseEntity<Void> enterChatRoom(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId) {
        String email;
        try {
            email = tokenProvider.getEmailFromToken(authorizationHeader);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header", e);
        }
        try {
            chatService.enterChatRoom(roomId, email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not enter chat room", e);
        }
    }

    // 채팅방 퇴장
    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<Void> leaveChatRoom(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String roomId) {
        String email;
        try {
            email = tokenProvider.getEmailFromToken(authorizationHeader);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header", e);
        }
        try {
            chatService.leaveChatRoom(roomId, email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not leave chat room", e);
        }
    }


}
