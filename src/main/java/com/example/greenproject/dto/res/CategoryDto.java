package com.example.greenproject.dto.res;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private CategoryDto parent;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
