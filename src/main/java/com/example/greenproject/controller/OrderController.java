package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreatePaymentRequest;
import com.example.greenproject.rabbitmq.PaymentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaymentProducer paymentProducer;

    @PostMapping("/pay")
    public String pay(@RequestBody CreatePaymentRequest createPaymentRequest) {
        paymentProducer.sendPaymentRequest(createPaymentRequest);
        return "Yêu cầu thanh toán đã được gửi đi.";
    }


}
