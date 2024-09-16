package com.example.greenproject.model;

import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.dto.res.VariationDtoWithOptions;
import com.example.greenproject.dto.res.VariationOptionDto;
import com.example.greenproject.dto.res.VariationOptionLazy;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "variation_id",referencedColumnName = "id")
    private Variation variation;
    private String value;

    public VariationOptionLazy mapToVariationOptionLazy(){
        return new VariationOptionLazy(id,value);
    }

    public VariationOptionDto mapToVariationOptionDto() {
        VariationOptionDto dto = new VariationOptionDto();
        dto.setId(id);
        dto.setValue(value);
        dto.setCreatedAt(getCreatedAt());
        dto.setUpdatedAt(getUpdatedAt());
        if(variation != null) {
            dto.setVariation(variation.mapToVariationDto());
        }
        return dto;
    }
}
