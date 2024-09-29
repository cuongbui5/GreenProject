package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateOrderRequest;
import com.example.greenproject.dto.req.CreateOrderRequestWithProductItem;
import com.example.greenproject.dto.req.CreatePaymentRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.rabbitmq.PaymentProducer;
import com.example.greenproject.service.OrderService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaymentProducer paymentProducer;
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestWithProductItem createOrderRequestWithProductItem) {
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                orderService.createOrderByProductItem(createOrderRequestWithProductItem)
        ));

    }


    @PostMapping("/pay")
    public String pay(@RequestBody CreatePaymentRequest createPaymentRequest) {
        paymentProducer.sendPaymentRequest(createPaymentRequest);
        return "Yêu cầu thanh toán đã được gửi đi.";
    }


}
