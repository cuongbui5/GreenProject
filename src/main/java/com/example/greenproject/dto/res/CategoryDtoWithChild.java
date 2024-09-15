package com.example.greenproject.dto.res;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
public class CategoryDtoWithChild {
    private Long id;
    private String name;
    private List<CategoryDtoWithChild> children;
    private Date createdAt;
    private Date updatedAt;

}
