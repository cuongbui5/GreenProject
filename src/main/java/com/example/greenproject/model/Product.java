package com.example.greenproject.model;

import com.example.greenproject.dto.res.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<Image> images=new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    @JsonIgnore
    private Category category;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductItem> productItems=new ArrayList<>();

    public ProductDto mapToProductDto() {
        ProductDto productDto=new ProductDto();
        if(category!=null){
            productDto.setCategory(category.mapToCategoryDtoLazy());
        }
        if(images!=null){
            productDto.setImages(images.stream().map(Image::mapToImageDto).toList());
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




    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();


    }





}
