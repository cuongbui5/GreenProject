package com.example.greenproject.model;

import com.example.greenproject.utils.Constants;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "_category")
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
    private Category parent;

    @ManyToMany(mappedBy = "categories",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JsonManagedReference("category_variation")
    private List<Variation> variations;

    public void addVariation(Variation variation){
        if(variations == null){
            variations = new ArrayList<>();
        }
        variation.getCategories().add(this);
        variations.add(variation);
    }

    public void removeVariation(Variation variation){
        if(variations.contains(variation)){
            variations.remove(variation);
            variation.getCategories().remove(this);
        }else{
            throw new RuntimeException("Variation " + variation.getName() + " don't exist in category " + this.getName());
        }
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
