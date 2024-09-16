package com.example.greenproject.model;

import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.model.enums.VoucherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;// Tên của voucher
    private String description;
    private Integer quantity;
    private Integer pointsRequired;
    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private VoucherType type; // Loại voucher (FREE_SHIP, DISCOUNT_PERCENTAGE, ...)

    private Double value; // Giá trị của voucher (ví dụ: 10%, 50.000 VND,...)

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



}
