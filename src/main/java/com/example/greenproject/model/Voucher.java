package com.example.greenproject.model;

import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.model.enums.VoucherType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "_voucher",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;// Tên của voucher
    private String description;
    private Integer quantity;
    private Integer pointsRequired;

    @Enumerated(EnumType.STRING)
    private VoucherType type;

    private Double value;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    public VoucherDto mapToVoucherDto() {
        VoucherDto dto = new VoucherDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setQuantity(quantity);
        dto.setPointsRequired(pointsRequired);
        dto.setType(type);
        dto.setValue(value);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setCreatedAt(getCreatedAt());
        dto.setUpdatedAt(getUpdatedAt());
        return dto;
    }

    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();


    }



}
