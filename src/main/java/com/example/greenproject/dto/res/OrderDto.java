package com.example.greenproject.dto.res;

import com.example.greenproject.model.Item;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
public class OrderDto {
    private Long id;
    private ContactDto contact;
    private List<ItemDto> items=new ArrayList<>();
    private boolean isPaid;
    private VoucherDto voucher;
    private Double productTotalCost;
    private Double shippingCost;
    private Double discountAmount;
    private Double totalCost;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;


}
