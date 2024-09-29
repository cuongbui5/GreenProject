package com.example.greenproject.dto.res;

import com.example.greenproject.model.VariationOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductItemDto {
    private Long id;
    private ProductDtoLazy product;
    private int quantity;
    private Double price;
    private Integer sold;
    private Integer reviewCount;
    private Integer totalRating;
    private Set<VariationOptionDtoLazy> variationOptions;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
