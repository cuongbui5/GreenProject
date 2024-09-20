package com.example.greenproject.model;

import com.example.greenproject.dto.res.CategoryDto;
import com.example.greenproject.dto.res.ProductDto;
import com.example.greenproject.utils.Utils;
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
    @Column(length = 1000)
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

    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();


    }





}
