package com.example.greenproject.dto.res;

import com.example.greenproject.model.enums.VoucherType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
public class VoucherDto {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Integer pointsRequired;
    private VoucherType type;
    private Double value;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
