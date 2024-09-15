package com.example.greenproject.dto.req_abstract;

import com.example.greenproject.model.enums.VoucherType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractVoucherRequest {
    private String name;
    private String description;
    private Integer quantity;
    private Integer pointsRequired;
    private Long version;
    private VoucherType type;
    private double value;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}