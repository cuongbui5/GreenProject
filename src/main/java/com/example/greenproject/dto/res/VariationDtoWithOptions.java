package com.example.greenproject.dto.res;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
@Data
public class VariationDtoWithOptions {
    private Long id;
    private String name;
    private List<VariationOptionLazy> values;
}
