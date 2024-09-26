package com.example.greenproject.model;

import com.example.greenproject.dto.res.*;
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
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<Image> images;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<ProductItem> productItems;




    public ProductDto mapToProductDto() {
        ProductDto productDto=new ProductDto();
        if(category!=null){
            productDto.setCategory(category.mapToCategoryDto());
        }
        productDto.setId(id);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setCreatedAt(getCreatedAt());
        productDto.setUpdatedAt(getUpdatedAt());
        List<ImageDto> imageDtos = images != null ? images.stream()
                .map(Image::mapToImageDto)
                .toList() : new ArrayList<>();
        productDto.setImages(imageDtos);

        return productDto;
    }

    public ProductDtoWithDetails mapToProductDtoWithDetails() {

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
        ProductDtoWithDetails dto=new ProductDtoWithDetails(id,name,description,category.mapToCategoryDto(),imageDtos,avgRating,minPrice,maxPrice);


        if(productItems!=null){
            dto.setProductItems(productItems.stream().map(ProductItem::mapToProductItemDtoLazy).toList());

        }
        return dto;

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
                description,
                category.mapToCategoryDto(),
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
