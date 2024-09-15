package com.example.greenproject.dto.res;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CategoryDtoWithParent {
    private Long id;
    private String name;
    private CategoryDtoWithParent parent;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;




}
