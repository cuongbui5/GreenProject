package com.example.greenproject.rabbitmq;


import com.example.greenproject.dto.req.CreatePaymentRequest;
import com.example.greenproject.service.OrderService;
import com.example.greenproject.utils.Constants;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    private final OrderService orderService;

    public PaymentConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "payment-queue")
    @Transactional
    public void receivePaymentRequest(CreatePaymentRequest createPaymentRequest) {
        orderService.createPayment(createPaymentRequest);
    }

}
