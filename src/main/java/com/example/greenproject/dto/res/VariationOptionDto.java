package com.example.greenproject.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VariationOptionDto {
    private Long id;
    private String value;
    private VariationDtoLazy variation;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
