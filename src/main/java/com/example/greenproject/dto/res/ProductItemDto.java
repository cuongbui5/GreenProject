package com.example.greenproject.dto.res;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Set;
@Data
public class ProductItemDto {
    private Long id;
    private ProductDto product;
    private int quantity;
    private Double price;
    private Integer sold;
    private Integer reviewCount;
    private Integer totalRating;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Set<VariationOptionDtoLazy> variationOptions;
}
