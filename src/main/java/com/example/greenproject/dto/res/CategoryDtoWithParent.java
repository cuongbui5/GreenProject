package com.example.greenproject.dto.res;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CategoryDtoWithParent {
    private Long id;
    private String name;
    private CategoryDtoWithParent parent;
    private Date createdAt;
    private Date updatedAt;




}
