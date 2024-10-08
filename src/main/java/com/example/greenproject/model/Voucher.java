package com.example.greenproject.model;

import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.model.enums.VoucherType;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private String name;
    private String description;
    private Integer quantity;
    private Integer pointsRequired;
    @Enumerated(EnumType.STRING)
    private VoucherType type;
    private Double value;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    @ManyToMany(mappedBy = "vouchers",fetch = FetchType.LAZY)
    private Set<User> users=new HashSet<>();
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


    public boolean validateVoucherValue() {
        if (type == VoucherType.FREE_SHIP) {
            return value == 0;
        } else if (type == VoucherType.DISCOUNT_PERCENTAGE) {
            return value >= 0 && value <= 100;
        } else if (type == VoucherType.DISCOUNT_AMOUNT) {
            return value > 0;
        }
        return false;
    }


    @PrePersist
    @PreUpdate
    public void trimData() {
        this.name = this.name.trim();

        if (!validateVoucherValue()) {
            throw new IllegalArgumentException("Invalid voucher value for type: " + type);
        }


    }



}
