package com.example.greenproject.dto.res;

import com.example.greenproject.model.Category;
import lombok.*;

import java.util.List;
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
