package com.be.friendy.warendy.domain.chat.service;

import com.be.friendy.warendy.domain.chat.dto.ChatRoomDto;
import com.be.friendy.warendy.domain.chat.dto.MessageDto;
import com.be.friendy.warendy.domain.chat.entity.ChatClient;
import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import com.be.friendy.warendy.domain.chat.entity.Message;
import com.be.friendy.warendy.domain.chat.repository.ChatClientRepository;
import com.be.friendy.warendy.domain.chat.repository.ChatRoomRepository;
import com.be.friendy.warendy.domain.chat.repository.MessageRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;
    //    private KafkaListenerEndpointRegistry registry;
    private final ConcurrentKafkaListenerContainerFactory<String, MessageDto> factory;
    private final WebSocketSessionService webSocketSessionService;
    private final Map<String, ConcurrentMessageListenerContainer<String, MessageDto>> containers = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    // 채팅방 생성
    public ChatRoom createChatRoom(String creatorId, String roomId, String name) {
        Member member;
        try {
            member = memberRepository.findByEmail(creatorId)
                    .orElseThrow(() -> new RuntimeException("member not exist"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid creator ID", e);
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .creator(member)
                .roomId(roomId)
                .memberNum(1)
                .name(name)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        try {
            createKafkaListener(roomId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create Kafka listener", e);
        }

        return savedChatRoom;
    }

    // 채팅방 삭제
    public void deleteChatRoom(String roomId, String requesterId) throws IOException {
        ChatRoom chatRoom;
        try {
            chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room ID", e);
        }

        if (!chatRoom.getCreator().getEmail().equals(requesterId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the creator can delete the chat room");
        }

        // ChatClient 제거
        chatClientRepository.deleteAll(chatClientRepository.findByChatRoom(chatRoom)
                .orElseThrow(() -> new RuntimeException("Chat room not found")));
        // Message 제거
        messageRepository.deleteAll(messageRepository.findByRoomIdOrderByTimestamp(roomId)
                .orElseThrow(() -> new RuntimeException("Message not found")));
        // ChatRoom 제거
        chatRoomRepository.delete(chatRoom);
        // 세션 제거
        webSocketSessionService.deleteChatRoom(roomId);
        // 카프카 리스너 제거
        try {
            removeKafkaListener(roomId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not remove Kafka listener", e);
        }

    }

    public ChatRoomDto getChatRoomInfo(String roomId){
        ChatRoom chatRoom;
        try {
            chatRoom = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room ID", e);
        }
        return ChatRoomDto.fromEntity(chatRoom);
    }

    // 메시지 전송
    public void sendMessage(String roomId, String senderId, String content) throws ExecutionException, InterruptedException {
        ChatRoom chatRoom;
        try {
            chatRoom = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room ID", e);
        }

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                .content(content)
                .sender(senderId)
                .roomId(roomId)
                .build();

        messageRepository.save(message);
        if (!message.getContent().isEmpty()) {
            // 카프카에 메세지를 push
            try {
                kafkaTemplate.send(chatRoom.getRoomId(), chatRoom.getRoomId(), MessageDto.fromEntity(message)).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not send message to Kafka", e);
            }
        }
    }

    public List<MessageDto> getChatMessages(String roomId, String userId) {
        List<MessageDto> messages;
        ChatRoom chatRoom;
        Member member;
        try {
            messages = MessageDto.fromEntityList(messageRepository.findByRoomIdOrderByTimestamp(roomId)
                    .orElseThrow(() -> new RuntimeException("Messages not found")));
            chatRoom = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
            member = memberRepository.findByEmail(userId)
                    .orElseThrow(() -> new RuntimeException("member not found"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request parameters", e);
        }

        ChatClient chatClient = chatClientRepository.findByChatRoomAndMember(chatRoom, member)
                .orElseThrow(() -> new RuntimeException("ChatClient not found"));
        return messages.subList(chatClient.getIdx(), messages.size());
    }

    // 채팅방 입장
    public void enterChatRoom(String roomId, String userId) {
        // DB에서 해당 유저를 채팅방에서 제거하는 로직
        ChatRoom chatRoom;
        try {
            chatRoom = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room ID", e);
        }


        // 채팅방 참여인원 업데이트
        if(chatRoom.getCreator().getEmail().equals(userId)){
            return;
        }else{
            chatRoom.setMemberNum(chatRoom.getMemberNum() + 1);
            chatRoomRepository.save(chatRoom);
        }
    }

    // 채팅방 퇴장
    public void leaveChatRoom(String roomId, String userId) {
        // DB에서 해당 유저를 채팅방에서 제거하는 로직
        ChatRoom chatRoom;

        Member member;
        try {
            chatRoom = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
            member = memberRepository.findByEmail(userId)
                    .orElseThrow(() -> new RuntimeException("member not found"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request parameters", e);
        }

        ChatClient chatClient = chatClientRepository.findByChatRoomAndMember(chatRoom, member)
                .orElseThrow(() -> new RuntimeException("Chat client not found"));
        if (chatClient != null) {
            chatClientRepository.delete(chatClient);
            chatRoom.setMemberNum(chatRoom.getMemberNum() - 1);
            chatRoomRepository.save(chatRoom);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chat client not found");
        }

        // 해당 채팅방의 모든 웹소켓 세션 중, userId가 일치하는 세션 찾기 및 연결 해제
        Set<WebSocketSession> sessions = webSocketSessionService.getSessions(roomId);
        for (WebSocketSession session : sessions) {
            if (userId.equals(session.getAttributes().get("email"))) {
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
        try {
            ConcurrentMessageListenerContainer<String, MessageDto> container = factory.createContainer(id);
            container.getContainerProperties().setMessageListener(new MessageListener<String, MessageDto>() {
                @Override
                public void onMessage(ConsumerRecord<String, MessageDto> record) {
                    String chatRoomId = record.key();  // assuming the chat room ID is the key of the record
                    if (chatRoomId == null) {
                        throw new IllegalArgumentException("Chat room ID is null");
                    }
                    MessageDto message = record.value();

                    // 이 부분은 message 객체를 어떻게 처리할지에 따라 달라질 수 있습니다.
                    // 예를 들어, message 객체가 text라는 속성을 가지고 있다면 아래와 같이 할 수 있습니다.
                    String jsonMessage = null;
                    try {
                        // 메시지 데이터를 JSON으로 만들기
                        jsonMessage = objectMapper.writeValueAsString(message);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("JSON objectMapping error!");
                    }
                    // content, 메시지 내용만 보내고 싶을 때 사용
                    if(jsonMessage==null){
                        String messageText = message.getContent();
                        jsonMessage=messageText;
                    }

                    for (WebSocketSession session : webSocketSessionService.getSessions(chatRoomId)) {
                        try {
//                            session.sendMessage(new TextMessage(messageText));
                            session.sendMessage(new TextMessage(jsonMessage));
                        } catch (IOException e) {
                            throw new RuntimeException("createKafkaListener error!");
                        }
                    }
                }
            });
            container.start();
            containers.put(id, container);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create Kafka listener", e);
        }
    }

    public void removeKafkaListener(String id) {
        ConcurrentMessageListenerContainer<String, MessageDto> container = containers.remove(id);
        if (container != null) {
            try {
                container.stop();
            } catch (RuntimeException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not stop Kafka listener", e);
            }
        }
    }

//    private String getEmail(String userId) {
//        // 쉼표 기준으로 문자열 분리
//        String[] infoParts = userId.split(", ");
//
//        // email 정보가 있는 부분을 찾는다.
//        String emailInfo = "";
//        for (String part : infoParts) {
//            if (part.startsWith("email=")) {
//                emailInfo = part;
//                break;
//            }
//        }
//
//        // email 정보 분리
//        String email = emailInfo.split("=")[1];
//        return email;
//    }
}
