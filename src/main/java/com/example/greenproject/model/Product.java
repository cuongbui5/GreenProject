package com.example.greenproject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 1000)
    private String description;
    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JsonManagedReference("product_image")
    private List<Image> images;
    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    public void addImage(Image image){
        if(images == null){
            images = new ArrayList<>();
        }
        image.setProduct(this);
        images.add(image);
    }

    public void addImage(List<Image> images){
        if(this.images == null){
            this.images = new ArrayList<>();
        }
        for(var image:images){
            image.setProduct(this);
            this.images.add(image);
        }
    }

    public void deleteImage(Long id){
        images.removeIf(image -> image.getId().equals(id));
    }
}
