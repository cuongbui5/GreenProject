package com.example.greenproject.model;

import com.example.greenproject.dto.res.CategoryDtoLazy;
import com.example.greenproject.dto.res.CategoryDto;
import com.example.greenproject.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "_category",  uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = Constants.MESSAGE_EMPTY)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",referencedColumnName = "id")
    @JsonIgnore
    private Category parent;


    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();


    }


    public CategoryDtoLazy mapToCategoryDtoLazy(){
        return new CategoryDtoLazy(id,name);
    }

    public CategoryDto mapToCategoryDto(){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName(name);
        categoryDto.setCreatedAt(getCreatedAt());
        categoryDto.setUpdatedAt(getUpdatedAt());
        if(parent != null){
            CategoryDto parentDto=new CategoryDto();
            parentDto.setId(parent.getId());
            parentDto.setName(parent.getName());
            parentDto.setCreatedAt(parent.getCreatedAt());
            parentDto.setUpdatedAt(parent.getUpdatedAt());
            categoryDto.setParent(parentDto);
        }

        return categoryDto;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
