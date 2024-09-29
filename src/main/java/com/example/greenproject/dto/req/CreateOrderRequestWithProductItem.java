package com.example.greenproject.dto.req;

import lombok.Data;

@Data
public class CreateOrderRequestWithProductItem {
    private Long productItemId;
    private Integer quantity;
}
