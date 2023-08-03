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
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatClientRepository chatClientRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private KafkaListenerEndpointRegistry registry;
    private final ConcurrentKafkaListenerContainerFactory<String, Message> factory;
    private final WebSocketSessionService webSocketSessionService;
    private final Map<String, ConcurrentMessageListenerContainer<String, Message>> containers = new ConcurrentHashMap<>();

    // 채팅방 생성
    public ChatRoom createChatRoom(String creatorId, String roomId, String name) {

        Member member = memberRepository.findByEmail(creatorId)
                .orElseThrow(() -> new RuntimeException("member not exist"));

        ChatRoom chatRoom = ChatRoom.builder()
                .creator(member)
                .roomId(roomId)
                .memberNum(0)
                .name(name)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        createKafkaListener(roomId);
        return savedChatRoom;
    }

    // 채팅방 삭제
    public void deleteChatRoom(String roomId, String requesterId) throws IOException {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (!chatRoom.getCreator().getEmail().equals(requesterId)) {
            throw new RuntimeException("Only the creator can delete the chat room");
        }

        // ChatClient 제거
        chatClientRepository.deleteAll(chatClientRepository.findByChatRoom(chatRoom));
        // Message 제거
        messageRepository.deleteAll(messageRepository.findByRoomIdOrderByTimestamp(roomId));
        // ChatRoom 제거
        chatRoomRepository.delete(chatRoom);
        // 세션 제거
        webSocketSessionService.deleteChatRoom(roomId);
        // 카프카 리스너 제거
        removeKafkaListener(roomId);

    }

    // 메시지 전송
    public void sendMessage(String roomId, String senderId, String content) throws ExecutionException, InterruptedException {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .timestamp(LocalDateTime.now().toString())
                .content(content)
                .sender(senderId)
                .roomId(roomId)
                .build();

        messageRepository.save(message);
        if (!message.getContent().isEmpty()) {
            // 카프카에 메세지를 push
            kafkaTemplate.send(chatRoom.getRoomId(), chatRoom.getRoomId(), message).get();
        }
    }

    public List<Message> getChatMessages(String roomId, String userId) {
        List<Message> messages = messageRepository.findByRoomIdOrderByTimestamp(roomId);
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);

        Member member = memberRepository.findByEmail(userId)
                .orElseThrow(() -> new RuntimeException("member not exist"));

        ChatClient chatClient = chatClientRepository.findByChatRoomAndMember(chatRoom, member);
        return messages.subList(chatClient.getIdx(), messages.size());
    }

    // 채팅방 입장
    public void enterChatRoom(String roomId, String userId) {
        // DB에서 해당 유저를 채팅방에서 제거하는 로직
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);

        // 채팅방 참여인원 업데이트
        chatRoom.setMemberNum(chatRoom.getMemberNum() + 1);
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 퇴장
    public void leaveChatRoom(String roomId, String userId) {
        // DB에서 해당 유저를 채팅방에서 제거하는 로직
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);

        Member member = memberRepository.findByEmail(userId)
                .orElseThrow(() -> new RuntimeException("member not exist"));

        ChatClient chatClient = chatClientRepository.findByChatRoomAndMember(chatRoom, member);
        if (chatClient != null) {
            chatClientRepository.delete(chatClient);
            chatRoom.setMemberNum(chatRoom.getMemberNum() - 1);
            chatRoomRepository.save(chatRoom);
        } else {
            throw new RuntimeException("Chat client not found");
        }

        // 해당 채팅방의 모든 웹소켓 세션 중, userId가 일치하는 세션 찾기 및 연결 해제
        Set<WebSocketSession> sessions = webSocketSessionService.getSessions(roomId);
        for (WebSocketSession session : sessions) {
            if (userId.equals(session.getPrincipal().getName())) {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                webSocketSessionService.unregisterSession(roomId, session);
            }
        }
    }

    public void createKafkaListener(String id) {
        ConcurrentMessageListenerContainer<String, Message> container = factory.createContainer(id);
        container.getContainerProperties().setMessageListener(new MessageListener<String, Message>() {
            @Override
            public void onMessage(ConsumerRecord<String, Message> record) {
                String chatRoomId = record.key();  // assuming the chat room ID is the key of the record
                if (chatRoomId == null) {
                    throw new IllegalArgumentException("Chat room ID is null");
                }
                Message message = record.value();

                // 이 부분은 message 객체를 어떻게 처리할지에 따라 달라질 수 있습니다.
                // 예를 들어, message 객체가 text라는 속성을 가지고 있다면 아래와 같이 할 수 있습니다.
                String messageText = message.getContent();

                for (WebSocketSession session : webSocketSessionService.getSessions(chatRoomId)) {
                    try {
                        session.sendMessage(new TextMessage(messageText));
                    } catch (IOException e) {
                        throw new RuntimeException("createKafkaListener error!");
                    }
                }
            }
        });
        container.start();
        containers.put(id, container);
    }

    public void removeKafkaListener(String id) {
        ConcurrentMessageListenerContainer<String, Message> container = containers.remove(id);
        if (container != null) {
            container.stop();
        }
    }
}
