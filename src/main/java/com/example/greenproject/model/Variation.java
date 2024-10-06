package com.example.greenproject.model;

import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.dto.res.VariationDtoLazy;
import com.example.greenproject.dto.res.VariationDtoWithOptions;
import com.example.greenproject.dto.res.VariationOptionDtoLazy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
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
    @JsonIgnore
    private Category category;
    private String name;

    @OneToMany(mappedBy = "variation",fetch = FetchType.LAZY)
    private Set<VariationOption> variationOptions;

    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();
    }

    public VariationDtoWithOptions mapToVariationDtoWithOptions(){
        VariationDtoWithOptions dto = new VariationDtoWithOptions();
        dto.setId(id);
        dto.setName(name);
        if(variationOptions != null){
           Set<VariationOptionDtoLazy> set=new HashSet<>();
           for(VariationOption option : variationOptions){
               set.add(option.mapToVariationOptionDtoLazy());
           }
           dto.setValues(set);

        }
        return dto;
    }

    public VariationDtoLazy mapToVariationDtoLazy(){
        return new VariationDtoLazy(id,name);
    }

    public VariationDto mapToVariationDto() {
        VariationDto variationDto=new VariationDto();
        variationDto.setId(id);
        variationDto.setName(name);
        variationDto.setCreatedAt(getCreatedAt());
        variationDto.setUpdatedAt(getUpdatedAt());
        if(category!=null){
            variationDto.setCategory(category.mapToCategoryDtoLazy());
        }

        return variationDto;
    }






}
