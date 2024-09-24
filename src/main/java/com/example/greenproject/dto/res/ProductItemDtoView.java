package com.example.greenproject.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItemDtoView {
    private Long id;
    private ProductDtoWithDetails product;
    private Double price;
    private Integer totalRating;
}
