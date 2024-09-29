package com.example.greenproject.dto.res;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Data
public class VariationDtoWithOptions {
    private Long id;
    private String name;
    private Set<VariationOptionDtoLazy> values;
}
