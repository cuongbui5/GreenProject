package com.example.greenproject.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDtoLazy {
    private Long id;
    private int quantity;
    private Double price;
    private Integer sold;
    private Integer reviewCount;
    private Integer totalRating;

}
