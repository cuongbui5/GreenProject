package com.example.greenproject.model;

import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.dto.res.VariationDtoWithOptions;
import com.example.greenproject.dto.res.VariationOptionDto;
import com.example.greenproject.dto.res.VariationOptionLazy;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "_variation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Variation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;
    private String name;

    @OneToMany(mappedBy = "variation")
    private Set<VariationOption> variationOptions;

    public VariationDtoWithOptions mapToVariationDtoWithOptions(){
        VariationDtoWithOptions dto = new VariationDtoWithOptions();
        dto.setId(id);
        dto.setName(name);
        if(variationOptions != null){
            List<VariationOptionLazy> options = new ArrayList<>();
            variationOptions.forEach(option -> {options.add(option.mapToVariationOptionLazy());});
            dto.setValues(options);

        }
        return dto;
    }

    public VariationDto mapToVariationDto() {
        VariationDto variationDto=new VariationDto();
        variationDto.setId(id);
        variationDto.setName(name);
        variationDto.setCreatedAt(getCreatedAt());
        variationDto.setUpdatedAt(getUpdatedAt());
        if(category!=null){
            variationDto.setCategory(category.mapToCategoryDto());
        }

        return variationDto;
    }






}
