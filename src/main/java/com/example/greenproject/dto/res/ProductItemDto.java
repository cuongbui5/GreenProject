package com.example.greenproject.dto.res;

import com.example.greenproject.model.VariationOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductItemDto {
    private Long id;
    private ProductDto product;
    private int quantity;
    private Double price;
    private List<VariationOptionDto> variationOptions;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
