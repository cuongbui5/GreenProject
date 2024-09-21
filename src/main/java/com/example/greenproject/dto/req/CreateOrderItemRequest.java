package com.example.greenproject.dto.req;

import lombok.Data;

@Data
public class CreateOrderItemRequest {
    private Long productItemId;
    private Long orderId;
    private int quantity;

}
