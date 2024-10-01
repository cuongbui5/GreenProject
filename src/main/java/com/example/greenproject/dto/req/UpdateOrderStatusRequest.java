package com.example.greenproject.dto.req;

import com.example.greenproject.model.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus orderStatus;
}
