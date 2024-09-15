package com.example.greenproject.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class VariationDto {
    private Long id;
    private String name;
    private CategoryDto category;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<VariationOptionDto> values;
}
