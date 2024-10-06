package com.example.greenproject.model;

import com.example.greenproject.dto.res.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_variation_option", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"value"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VariationOption extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id",referencedColumnName = "id")
    @JsonIgnore
    private Variation variation;
    private String value;
    @ManyToMany(mappedBy = "variationOptions",fetch = FetchType.LAZY)
    private Set<ProductItem> productItems = new HashSet<>();



    @PrePersist
    @PreUpdate
    public void trimData() {
        this.value = this.value.trim();


    }


    public VariationOptionDto mapToVariationOptionDto() {
        VariationOptionDto dto = new VariationOptionDto();
        dto.setId(id);
        dto.setValue(value);
        dto.setCreatedAt(getCreatedAt());
        dto.setUpdatedAt(getUpdatedAt());
        if(variation != null) {
            dto.setVariation(variation.mapToVariationDtoLazy());
        }
        return dto;
    }

    public VariationOptionDtoLazy mapToVariationOptionDtoLazy() {
        return new VariationOptionDtoLazy(id,value);
    }
}
