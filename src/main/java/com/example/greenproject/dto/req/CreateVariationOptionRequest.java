package com.example.greenproject.dto.req;

import com.example.greenproject.model.Variation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateVariationOptionRequest {
    private Long variationId;
    private String values;
}
