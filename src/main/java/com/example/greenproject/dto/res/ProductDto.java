package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private CategoryDtoLazy category;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<ImageDto> images;

}
