package com.ai_project.dataart.config;

import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.UserRepository;
import com.ai_project.dataart.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:63342", "http://localhost:8080") // Додай сюди порт свого фронта
                .withSockJS();
    }

    // Додаємо перехоплювач для STOMP повідомлень
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Перевіряємо, чи це запит на підключення
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");

                    if (authorization != null && !authorization.isEmpty()) {
                        String token = authorization.get(0);
                        if (token.startsWith("Bearer ")) {
                            token = token.substring(7);
                            String username = jwtService.extractUsername(token);

                            if (username != null) {
                                User user = userRepository.findByUsername(username).orElse(null);

                                if (user != null && jwtService.isTokenValid(token, user.getUsername())) {
                                    // Авторизуємо користувача для WebSocket сесії
                                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                            user, null, user.getAuthorities()
                                    );
                                    accessor.setUser(auth);
                                }
                            }
                        }
                    }
                }
                return message;
            }
        });
    }
}