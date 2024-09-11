package com.example.greenproject.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class VariationDto {
    private Long id;
    private String name;
    private List<VariationOptionDto> values;
}
