package com.example.greenproject.controller;

import com.example.greenproject.dto.req.*;
import com.example.greenproject.dto.res.BaseResponse;
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

    @PostMapping("/set-contact")
    public ResponseEntity<?> setContactToOrder(@RequestBody UpdateContactOrderRequest updateContactOrderRequest){
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                orderService.updateContactToOrder(updateContactOrderRequest)
        ));
    }

    @PostMapping("/set-voucher")
    public ResponseEntity<?> setVoucherToOrder(@RequestBody UpdateVoucherOrderRequest updateVoucherOrderRequest){
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                orderService.updateVoucherToOrder(updateVoucherOrderRequest)
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.ok().body(new BaseResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE
        ));
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@RequestBody UpdateOrderStatusRequest updateOrderStatusRequest) {

        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                orderService.updateOrderStatus(id,updateOrderStatusRequest)
        ));
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                orderService.getOrderById(id)
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestWithProductItem createOrderRequestWithProductItem) {
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                orderService.createOrderByProductItem(createOrderRequestWithProductItem)
        ));

    }

    @PostMapping("/createByCart")
    public ResponseEntity<?> createOrderByCart() {
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                orderService.createOrderByCart()
        ));

    }


    @PostMapping("/pay")
    public String pay(@RequestBody CreatePaymentRequest createPaymentRequest) {
        paymentProducer.sendPaymentRequest(createPaymentRequest);
        return "Yêu cầu thanh toán đã được gửi đi.";
    }


}
