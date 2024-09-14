package com.example.greenproject.model;

import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.dto.res.VariationOptionDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "_variation")
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variation variation)) return false;
        return Objects.equals(id, variation.id) && Objects.equals(name, variation.name);
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }



}
