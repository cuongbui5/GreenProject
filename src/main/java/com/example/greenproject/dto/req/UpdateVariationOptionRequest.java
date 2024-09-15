package com.example.greenproject.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVariationOptionRequest {
    private Long variationId;
    private String value;
}
