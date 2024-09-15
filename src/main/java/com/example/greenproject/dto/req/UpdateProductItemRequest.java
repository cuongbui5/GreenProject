package com.example.greenproject.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UpdateProductItemRequest {
    private Integer quantity;
    private Double price;
    private List<Long> productConfig;
}
