package com.example.greenproject.dto.req;

import com.example.greenproject.model.enums.VoucherType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilteringVoucherRequest {
    private String name;
    private VoucherType type;
    private Boolean isActive;
}
