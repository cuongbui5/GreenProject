package com.example.greenproject.model;
import com.example.greenproject.dto.res.*;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.HashSet;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    @JsonIgnore
    private Product product;
    private Integer quantity;
    private Double price;
    private Integer sold=0;
    private Integer reviewsCount=0;
    private Integer totalRating=0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "_product_configuration",
            joinColumns = @JoinColumn(name = "product_item_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "variation_option_id",
                    referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<VariationOption> variationOptions=new HashSet<>();

    public ProductItemDtoLazy mapToProductItemDtoLazy(){
        ProductItemDtoLazy dto = new ProductItemDtoLazy();
        dto.setId(id);
        dto.setQuantity(quantity);
        dto.setPrice(price);
        dto.setSold(sold);
        dto.setTotalRating(totalRating);
        dto.setReviewCount(reviewsCount);
        return dto;
    }

    public ProductItemDtoDetail mapToProductItemDtoDetail(){
        ProductItemDtoDetail dto = new ProductItemDtoDetail();
        dto.setId(id);
        dto.setQuantity(quantity);
        dto.setPrice(price);
        dto.setSold(sold);
        dto.setTotalRating(totalRating);
        dto.setReviewCount(reviewsCount);
        if(!variationOptions.isEmpty()){
            Set<VariationOptionDto> dtoSet=new HashSet<>();
            variationOptions.forEach(variationOptionDto -> {
                dtoSet.add(variationOptionDto.mapToVariationOptionDto());
            });
            dto.setVariationOptions(dtoSet);
        }

        return dto;
    }

    public ProductItemDtoLazyWithProduct mapToProductItemDtoLazyWithProduct(){
        ProductItemDtoLazyWithProduct dto=new ProductItemDtoLazyWithProduct();
        dto.setId(id);
        dto.setQuantity(quantity);
        dto.setPrice(price);
        dto.setCreatedAt(getCreatedAt());
        dto.setUpdatedAt(getUpdatedAt());
        dto.setSold(sold);
        dto.setTotalRating(totalRating);
        dto.setReviewCount(reviewsCount);
        if(product!=null){
            dto.setProduct(product.mapToProductDtoLazy());
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
        if(!variationOptions.isEmpty()){
            Set<VariationOptionDtoLazy> dtoSet=new HashSet<>();
            variationOptions.forEach(variationOptionDto -> {
                dtoSet.add(variationOptionDto.mapToVariationOptionDtoLazy());
            });
            dto.setVariationOptions(dtoSet);
        }
        return dto;

    }

}
