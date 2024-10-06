package com.example.greenproject.model;

import com.example.greenproject.dto.res.ImageDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    public ImageDto mapToImageDto() {
        return new ImageDto(id,url);
    }
}
