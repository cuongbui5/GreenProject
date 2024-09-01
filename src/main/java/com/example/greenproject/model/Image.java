package com.example.greenproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_image")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Image extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    @JsonBackReference("product_image")
    private Product product;

}
