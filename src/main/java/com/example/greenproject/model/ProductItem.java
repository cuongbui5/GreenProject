package com.example.greenproject.model;
import com.example.greenproject.dto.res.ProductItemDto;
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
public class ProductItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;
    private Integer quantity;
    private Double price;
    @Version
    private Long version;
    @ManyToMany
    @JoinTable(name = "_product_configuration",
            joinColumns = @JoinColumn(name = "product_item_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "variation_option_id",
                    referencedColumnName = "id")
    )
    private List<VariationOption> variationOptions=new ArrayList<>();

    public ProductItemDto mapToProductItemDto(){
        ProductItemDto dto=new ProductItemDto();
        dto.setId(id);
        dto.setQuantity(quantity);
        dto.setPrice(price);
        dto.setCreatedAt(getCreatedAt());
        dto.setUpdatedAt(getUpdatedAt());
        if(product!=null){
            dto.setProduct(product.mapToProductDto());
        }
        if(variationOptions!=null){
            dto.setVariationOptions(variationOptions.stream().map(VariationOption::mapToVariationOptionDto).toList());
        }
        return dto;

    }


}
