package com.example.greenproject.model;

import com.example.greenproject.dto.res.CategoryDto;
import com.example.greenproject.dto.res.ImageDto;
import com.example.greenproject.dto.res.ProductDto;
import com.example.greenproject.dto.res.ProductDtoView;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_product",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 10000)
    private String description;
    @OneToMany(mappedBy = "product")
    private List<Image> images;
    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;
    @OneToMany(mappedBy = "product")
    private List<ProductItem> productItems;




    public ProductDto mapToProductDto() {
        CategoryDto categoryDto=null;
        if(category!=null){
            categoryDto=new CategoryDto(category.getId(),category.getName());
        }
        ProductDto productDto=new ProductDto();
        productDto.setCategory(categoryDto);
        productDto.setId(id);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setCreatedAt(getCreatedAt());
        productDto.setUpdatedAt(getUpdatedAt());

        return productDto;
    }

    public ProductDtoView mapToProductDtoView() {
        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
        double totalRating = 0;
        int totalReviews = 0;

        // Check if product has any product items
        if (productItems != null && !productItems.isEmpty()) {
            for (ProductItem item : productItems) {
                if (item.getPrice() != null) {
                    minPrice = Math.min(minPrice, item.getPrice());
                    maxPrice = Math.max(maxPrice, item.getPrice());
                }
                totalRating += item.getTotalRating();
                totalReviews += item.getReviewsCount();
            }
        }

        // Handle case where there are no valid prices
        if (minPrice == Double.MAX_VALUE) {
            minPrice = 0.0;
        }
        if (maxPrice == Double.MIN_VALUE) {
            maxPrice = 0.0;
        }

        // Calculate average rating
        Double avgRating = totalReviews > 0 ? totalRating / totalReviews : 0.0;

        // Map image DTOs
        List<ImageDto> imageDtos = images != null ? images.stream()
                .map(Image::mapToImageDto)
                .toList() : new ArrayList<>();

        // Create ProductDtoView
        return new ProductDtoView(
                id,
                name,
                imageDtos,
                minPrice,
                maxPrice,
                avgRating
        );
    }

    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();


    }





}
