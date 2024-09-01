package com.example.greenproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_variation_option")
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
    @JsonBackReference("variation_variationOption")
    private Variation variation;
    private String value;
}
