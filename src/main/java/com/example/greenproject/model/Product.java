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
            productDto.setCategory(category.mapToCategoryDtoLazy());
        }
        productDto.setId(id);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setCreatedAt(getCreatedAt());
        productDto.setUpdatedAt(getUpdatedAt());
        return productDto;
    }

    public ProductDtoLazy mapToProductDtoLazy() {
        return new ProductDtoLazy(id,name);
    }

    public ProductDtoWithDetails mapToProductDtoWithDetails() {
        List<ImageDto> imageDtos = images != null ? images.stream()
                .map(Image::mapToImageDto)
                .toList() : new ArrayList<>();

        ProductDtoWithDetails dto=new ProductDtoWithDetails(id,name,description,category.mapToCategoryDtoLazy(),getCreatedAt(),getUpdatedAt(),imageDtos);

        if(productItems!=null){
            dto.setProductItems(productItems.stream().map(ProductItem::mapToProductItemDtoLazy).toList());

        }
        return dto;

    }



    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();


    }





}
