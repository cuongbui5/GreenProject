package com.example.greenproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonBackReference("category_variation")
    private Set<Category> categories;
    private String name;

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
