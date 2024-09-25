package com.example.greenproject.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoWithDetails extends ProductDtoView{
    public ProductDtoWithDetails(Long id, String name, String description, CategoryDto category, List<ImageDto> images, Double minPrice, Double maxPrice, Double avgRating) {
        super(id, name, description, category, images, minPrice, maxPrice, avgRating);
    }

    private List<ProductItemDtoLazy> productItems;

}
