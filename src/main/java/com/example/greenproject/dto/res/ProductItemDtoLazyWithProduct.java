package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductItemDtoLazyWithProduct {
    private Long id;
    private ProductDtoLazy product;
    private int quantity;
    private Double price;
    private Integer sold;
    private Integer reviewCount;
    private Integer totalRating;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
