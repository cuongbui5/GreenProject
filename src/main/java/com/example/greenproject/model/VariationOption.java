package com.example.greenproject.model;

import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.dto.res.VariationOptionDto;
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
    private Variation variation;
    private String value;

    public VariationOptionDto mapToVariationOptionDto() {
        VariationOptionDto dto = new VariationOptionDto();
        dto.setId(id);
        dto.setValue(value);
        return dto;
    }
}
