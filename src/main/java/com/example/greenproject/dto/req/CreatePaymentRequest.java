package com.example.greenproject.dto.req;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long orderId;
    private Long paymentAccountId;
    private String pin;
}
