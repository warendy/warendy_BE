package com.be.friendy.warendy.domain.chat.service;

import com.be.friendy.warendy.domain.chat.dto.MessageDto;
import com.be.friendy.warendy.domain.chat.entity.ChatClient;
import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import com.be.friendy.warendy.domain.chat.entity.Message;
import com.be.friendy.warendy.domain.chat.repository.ChatClientRepository;
import com.be.friendy.warendy.domain.chat.repository.ChatRoomRepository;
import com.be.friendy.warendy.domain.chat.repository.MessageRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final ChatService chatService;
    private final ChatClientRepository chatClientRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final WebSocketSessionService webSocketSessionService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            // URL의 path parameter로부터 채팅방 ID를 추출
            String roomId = session.getUri().getPath().split("/")[4];
            webSocketSessionService.enterChatRoom(roomId, session);
            String email = (String) session.getAttributes().get("email");

            // 이전 채팅 내역을 불러옵니다.
//            String email = getEmail(session.getPrincipal().getName()); // 사용자 인증을 구현한 방법에 따라 사용자 ID를 얻는 방법은 달라질 수 있습니다.(이건 JWT 사용시 사용)
//        String userId = session.getUri().getPath().split("/")[5];

            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("member not found"));

            ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
            ChatClient chatClient = chatClientRepository.findByChatRoomAndMember(chatRoom, member)
                    .orElse(null);

            if (chatClient == null) {
                List<Message> messages = messageRepository.findByRoomIdOrderByTimestamp(roomId)
                        .orElseThrow(() -> new RuntimeException("Messages not found"));
                chatClient = ChatClient.builder()
                        .member(member)
                        .chatRoom(chatRoom)
                        .idx(messages.size())
                        .build();

                chatClientRepository.save(chatClient);
                chatService.sendMessage(roomId, email, email + "님이 입장하였습니다.");
                return;
            }

            List<MessageDto> previousMessages = chatService.getChatMessages(roomId, email);
            for (MessageDto previousMessage : previousMessages) {
                session.sendMessage(new TextMessage(previousMessage.getContent()));
            }
        } catch (RuntimeException e) {
            log.error("RuntimeException in establishing connection : ", e);
        } catch (IOException e) {
            log.error("IOException in establishing connection : ", e);
        } catch (ExecutionException e) {
            log.error("ExecutionException in establishing connection : ", e);
        } catch (InterruptedException e) {
            log.error("InterruptedException in establishing connection : ", e);
        } catch (Exception e) {
            log.error("Error in establishing connection : ", e);
        }

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // 채팅 메시지를 Kafka로 전송하는 로직을 추가
            String roomId = session.getUri().getPath().split("/")[4];
            String userId = (String) session.getAttributes().get("email"); // 사용자 인증을 구현한 방법에 따라 사용자 ID를 얻는 방법은 달라질 수 있습니다.
//        String userId = "dlduddnjs198";
//        String userId = session.getUri().getPath().split("/")[5];
            String content = message.getPayload();

            // 채팅 메시지를 DB에 저장하고 Kafka로 전송하는 로직
            chatService.sendMessage(roomId, userId, content);
        } catch (ExecutionException e) {
            log.error("ExecutionException in handling text message : ", e);
        } catch (InterruptedException e) {
            log.error("InterruptedException in handling text message : ", e);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            // URL의 path parameter로부터 채팅방 ID를 추출
            String roomId = session.getUri().getPath().split("/")[4];

            webSocketSessionService.disconnectChatRoom(roomId, session);
        } catch (Exception e) { // 이 경우에는 특정 예외를 정확히 알 수 없으므로, 일반 Exception 사용
            log.error("Error in closing connection : ", e);
        }

    }
}
