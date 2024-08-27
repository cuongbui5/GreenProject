package com.example.greenproject.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FilteringProductRequest {
    private String name;
    private Long categoryId;
    private int pageNum;
    private int pageSize;
}
