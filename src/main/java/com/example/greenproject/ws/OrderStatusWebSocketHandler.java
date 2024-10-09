package com.example.greenproject.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Slf4j
public class OrderStatusWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Message:{}", message.getPayload());
        session.sendMessage(new TextMessage("Hello my friend"));

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Xử lý tin nhắn từ client nếu cần
    }

    public void notifyOrderStatus(String orderId, String status, WebSocketSession session) throws Exception {
        String notification = "Order " + orderId + " status: " + status;
        session.sendMessage(new TextMessage(notification));
    }
}
