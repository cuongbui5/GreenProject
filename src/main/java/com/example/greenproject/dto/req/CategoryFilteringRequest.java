package com.example.greenproject.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryFilteringRequest {
    private String name;
    private String sortOption;
    private int pageNum;
    private int pageSize;
}
