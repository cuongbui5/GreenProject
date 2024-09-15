package com.example.greenproject.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
public class CreateProductItemRequest {
    private Long productId;
    private int quantity;
    private Double price;
    private List<Long> productConfig;
}
