package com.example.greenproject.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateProductRequest {
    private Long id;
    private String name;
    private String description;
    private List<String> imagesUrl;
    private Long categoryId;
}
