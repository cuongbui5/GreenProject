package com.example.greenproject.model;
import com.example.greenproject.dto.res.ProductItemDto;
import com.example.greenproject.dto.res.ProductItemDtoLazy;
import com.example.greenproject.dto.res.ProductItemDtoView;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "_product_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;
    private Integer quantity;
    private Double price;
    private Integer sold=0;
    private Integer reviewsCount=0;
    private Integer totalRating=0;

    @Version
    private Long version;
    @ManyToMany
    @JoinTable(name = "_product_configuration",
            joinColumns = @JoinColumn(name = "product_item_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "variation_option_id",
                    referencedColumnName = "id")
    )
    private Set<VariationOption> variationOptions=new HashSet<>();

    public ProductItemDtoLazy mapToProductItemDtoLazy(){
        ProductItemDtoLazy dto = new ProductItemDtoLazy();
        dto.setId(id);
        dto.setQuantity(quantity);
        dto.setPrice(price);
        dto.setSold(sold);
        dto.setTotalRating(totalRating);
        dto.setReviewCount(reviewsCount);
        if(variationOptions!=null){
            dto.setVariationOptions(variationOptions.stream().map(VariationOption::mapToVariationOptionDto).toList());
        }
        return dto;
    }

    public ProductItemDto mapToProductItemDto(){
        ProductItemDto dto=new ProductItemDto();
        dto.setId(id);
        dto.setQuantity(quantity);
        dto.setPrice(price);
        dto.setCreatedAt(getCreatedAt());
        dto.setUpdatedAt(getUpdatedAt());
        dto.setSold(sold);
        dto.setTotalRating(totalRating);
        dto.setReviewCount(reviewsCount);
        if(product!=null){
            dto.setProduct(product.mapToProductDto());
        }
        if(variationOptions!=null){
            dto.setVariationOptions(variationOptions.stream().map(VariationOption::mapToVariationOptionDto).toList());
        }
        return dto;

    }

}
