package com.be.friendy.warendy.domain.chat.configuration;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SessionHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 헤더에서 JWT 토큰을 가져옵니다.
        String token = request.getHeaders().get("Authorization").get(0);

        // JWT 토큰을 검증하고 토큰에 저장된 사용자 정보를 가져옵니다.
        // 토큰 검증 실패 시 예외를 던져서 WebSocket 연결을 중지할 수 있습니다.
        // 여기서는 JWT 토큰을 처리하는 서비스가 이미 존재한다고 가정합니다.
        String email = tokenProvider.getEmailFromToken(token);

        // WebSocket 세션에서 사용할 수 있도록 사용자 정보를 attributes에 저장합니다.
        attributes.put("email", email);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        // 아무 것도 하지 않습니다.
    }
}
