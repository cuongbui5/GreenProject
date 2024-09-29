package com.example.greenproject.dto.res;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Data
@Getter
@Setter
public class ProductItemDtoDetail {
    private Long id;
    private int quantity;
    private Double price;
    private Integer sold;
    private Integer reviewCount;
    private Integer totalRating;
    private Set<VariationOptionDto> variationOptions;
}
