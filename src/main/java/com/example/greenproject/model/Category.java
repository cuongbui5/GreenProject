package com.example.greenproject.model;

import com.example.greenproject.dto.res.CategoryDto;
import com.example.greenproject.dto.res.CategoryDtoWithChild;
import com.example.greenproject.dto.res.CategoryDtoWithParent;
import com.example.greenproject.utils.Constants;
import com.example.greenproject.utils.Utils;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "parent_id",referencedColumnName = "id")
    @JsonBackReference("parent_child")
    private Category parent;
    @OneToMany(mappedBy = "parent",fetch = FetchType.EAGER)
    @JsonBackReference("parent_child")
    private List<Category> children=new ArrayList<>();
    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();


    }


    public CategoryDto mapToCategoryDto(){
        return new CategoryDto(id,name);
    }

    public CategoryDtoWithParent mapToCategoryDtoWithParent(){
        CategoryDtoWithParent categoryDtoWithParent = new CategoryDtoWithParent();
        categoryDtoWithParent.setId(id);
        categoryDtoWithParent.setName(name);
        categoryDtoWithParent.setCreatedAt(getCreatedAt());
        categoryDtoWithParent.setUpdatedAt(getUpdatedAt());
        if(parent != null){
            CategoryDtoWithParent parentDto=new CategoryDtoWithParent();
            parentDto.setId(parent.getId());
            parentDto.setName(parent.getName());
            parentDto.setCreatedAt(parent.getCreatedAt());
            parentDto.setUpdatedAt(parent.getUpdatedAt());
            categoryDtoWithParent.setParent(parentDto);
        }

        return categoryDtoWithParent;
    }
    public CategoryDtoWithChild mapToCategoryDtoWithChild(){
        CategoryDtoWithChild categoryDtoWithChild = new CategoryDtoWithChild();
        categoryDtoWithChild.setId(id);
        categoryDtoWithChild.setName(name);
        if(children.isEmpty()){
            categoryDtoWithChild.setChildren(null);
        }else {
            categoryDtoWithChild.setChildren(children.stream().map(Category::mapToCategoryDtoWithChild).toList());
        }

        return categoryDtoWithChild;
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
