package com.example.greenproject.dto.res;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "product_dto_view")
public class ProductDtoView {
    @Id
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Integer sold;
    private Double minPrice;
    private Double maxPrice;
    private Integer totalRating;
    private Integer totalReviews;
    private String imageCover;
}
