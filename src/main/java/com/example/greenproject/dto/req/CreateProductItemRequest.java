package com.example.greenproject.dto.req;

import com.example.greenproject.model.Product;
import com.example.greenproject.model.VariationOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class CreateProductItemRequest {
    private Long productId;
    private int quantity;
    private Double price;
    private List<Long> variationOptionsId;
}
