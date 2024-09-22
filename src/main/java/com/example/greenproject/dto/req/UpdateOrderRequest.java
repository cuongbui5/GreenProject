package com.example.greenproject.dto.req;

import com.example.greenproject.model.Order;
import com.example.greenproject.model.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private OrderStatus orderStatus;
}
