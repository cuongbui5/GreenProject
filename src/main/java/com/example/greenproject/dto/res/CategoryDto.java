package com.example.greenproject.dto.res;

import lombok.*;

import java.time.ZonedDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private CategoryDto parent;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;




}
