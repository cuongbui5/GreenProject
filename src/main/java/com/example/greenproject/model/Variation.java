package com.example.greenproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
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
    @ManyToMany
    @JoinTable(name = "_variation_category",
            joinColumns = @JoinColumn(name = "variation_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;
    private String name;

    @OneToMany(mappedBy = "variation")
    private Set<VariationOption> variationOptions;

    public void addCategory(Category category){
        if(categories == null){
            categories = new HashSet<>();
        }
        categories.add(category);
    }

    public void removeCategory(Category category){
        if(categories.contains(category)){
            categories.remove(category);
        }else{
            throw new RuntimeException("Category " + category.getName() + " don't exist in variation " + this.getName());
        }
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
