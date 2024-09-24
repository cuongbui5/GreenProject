package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoWithDetails {
    private Long id;
    private String name;
    private String description;
    private CategoryDto category;
    private List<ImageDto> images;
    private List<ProductItemDtoLazy> productItems;

}
