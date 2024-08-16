package com.example.greenproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_variation_option")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VariationOption extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "variation_id",referencedColumnName = "id")
    private Variation variation;
    private String value;
}
