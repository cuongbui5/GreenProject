package com.example.greenproject.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
//WebSocketConfigurer
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /*@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderStatusWebSocketHandler(), "/order-status")
                .setAllowedOrigins("*");
    }
    @Bean
    public WebSocketHandler orderStatusWebSocketHandler() {
        return new OrderStatusWebSocketHandler();
    }*/

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
       // Thêm dòng này
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Điểm kết nối cho WebSocket
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
