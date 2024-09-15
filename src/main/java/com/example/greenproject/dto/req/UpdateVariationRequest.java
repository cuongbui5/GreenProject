package com.example.greenproject.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVariationRequest {
    private String name;
    private Long categoryId;
}
