package com.example.greenproject.dto.res;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoWithDetails extends ProductDto {
    public ProductDtoWithDetails(Long id, String name, String description, CategoryDtoLazy category, ZonedDateTime createdAt, ZonedDateTime updatedAt, List<ImageDto> images) {
        super(id, name, description, category, createdAt, updatedAt, images);
    }

    private List<ProductItemDtoLazy> productItems;

}
