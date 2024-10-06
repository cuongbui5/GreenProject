package com.example.greenproject.dto.res;

import com.example.greenproject.model.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderDtoLazy {
    private Long id;
    private Double productTotalCost;
    private Double shippingCost;
    private Double totalCost;
    private Double discountAmount;
    private OrderStatus status;
}
