package com.example.greenproject.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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
    private int quantity;
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
    private Set<VariationOption> variationOptions=new HashSet<>();


}
