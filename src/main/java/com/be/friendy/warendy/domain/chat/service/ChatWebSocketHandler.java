package com.be.friendy.warendy.domain.chat.service;

import com.be.friendy.warendy.domain.chat.entity.ChatClient;
import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import com.be.friendy.warendy.domain.chat.entity.Message;
import com.be.friendy.warendy.domain.chat.repository.ChatClientRepository;
import com.be.friendy.warendy.domain.chat.repository.ChatRoomRepository;
import com.be.friendy.warendy.domain.chat.repository.MessageRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final ChatService chatService;
    private final ChatClientRepository chatClientRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final WebSocketSessionService webSocketSessionService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException, ExecutionException, InterruptedException {
        // URL의 path parameter로부터 채팅방 ID를 추출
        String roomId = session.getUri().getPath().split("/")[4];
        webSocketSessionService.enterChatRoom(roomId, session);

        // 이전 채팅 내역을 불러옵니다.
        String email = getEmail(session.getPrincipal().getName()); // 사용자 인증을 구현한 방법에 따라 사용자 ID를 얻는 방법은 달라질 수 있습니다.(이건 JWT 사용시 사용)
//        String userId = session.getUri().getPath().split("/")[5];

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("member not exist"));

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        ChatClient chatClient = chatClientRepository.findByChatRoomAndMember(chatRoom, member);

        if (chatClient == null) {
            List<Message> messages = messageRepository.findByRoomIdOrderByTimestamp(roomId);
            chatClient = ChatClient.builder()
                    .member(member)
                    .chatRoom(chatRoom)
                    .idx(messages.size())
                    .build();

            chatClient = chatClientRepository.save(chatClient);
            chatService.sendMessage(roomId, email, email + "님이 입장하였습니다.");
            return;
        }

        List<Message> previousMessages = chatService.getChatMessages(roomId, email);
        for (Message previousMessage : previousMessages) {
            session.sendMessage(new TextMessage(previousMessage.getContent()));
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws ExecutionException, InterruptedException {
        // 채팅 메시지를 Kafka로 전송하는 로직을 추가
        String roomId = session.getUri().getPath().split("/")[4];
        String userId = getEmail(session.getPrincipal().getName()); // 사용자 인증을 구현한 방법에 따라 사용자 ID를 얻는 방법은 달라질 수 있습니다.
//        String userId = "dlduddnjs198";
//        String userId = session.getUri().getPath().split("/")[5];
        String content = message.getPayload();

        // 채팅 메시지를 DB에 저장하고 Kafka로 전송하는 로직
        chatService.sendMessage(roomId, userId, content);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // URL의 path parameter로부터 채팅방 ID를 추출
        String roomId = session.getUri().getPath().split("/")[4];

        webSocketSessionService.disconnectChatRoom(roomId, session);
    }

    private String getEmail(String userId) {
        // 쉼표 기준으로 문자열 분리
        String[] infoParts = userId.split(", ");

        // email 정보가 있는 부분을 찾는다.
        String emailInfo = "";
        for (String part : infoParts) {
            if (part.startsWith("email=")) {
                emailInfo = part;
                break;
            }
        }

        // email 정보 분리
        String email = emailInfo.split("=")[1];
        return email;
    }
}
