package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoView {
    private Long id;
    private String name;
    private List<ImageDto> images;
    private Double minPrice;
    private Double maxPrice;
    private Double avgRating;
}
