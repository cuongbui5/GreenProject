package com.example.greenproject.rabbitmq;

import com.example.greenproject.dto.req.CreatePaymentRequest;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Service
public class PaymentProducer {
    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentRequest(CreatePaymentRequest createPaymentRequest) {
        String exchange = "payment-exchange";
        String routingKey = "payment-key";
        rabbitTemplate.convertAndSend(exchange, routingKey, createPaymentRequest);
    }




}
