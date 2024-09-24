package com.example.greenproject.dto.res;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class ProductItemDtoView {
    private Long id;
    private ProductDtoWithDetails product;
    private int quantity;
    private Double price;
    private Integer sold;
    private Integer reviewCount;
    private Integer totalRating;
    private List<VariationOptionDto> variationOptions;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
