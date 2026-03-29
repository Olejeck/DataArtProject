package com.ai_project.dataart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Канал для повідомлень від сервера до клієнта
        config.enableSimpleBroker("/topic", "/queue");
        // Префікс для повідомлень від клієнта до сервера
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Точка підключення для браузера
        registry.addEndpoint("/ws-chat").withSockJS();
    }
}