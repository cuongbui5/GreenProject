package com.example.greenproject.dto.req;

import lombok.Data;

@Data
public class UpdateVoucherOrderRequest {
    private Long orderId;
    private Long voucherId;
}
