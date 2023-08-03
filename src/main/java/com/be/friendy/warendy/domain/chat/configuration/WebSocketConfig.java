package com.be.friendy.warendy.domain.chat.configuration;

import com.be.friendy.warendy.domain.chat.service.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(chatWebSocketHandler, "/api/chat/rooms/{roomId}").setAllowedOrigins("*");
        // userId 어캐받지?
        registry.addHandler(chatWebSocketHandler, "/api/chat/rooms/{roomId}").setAllowedOrigins("*");
    }
}