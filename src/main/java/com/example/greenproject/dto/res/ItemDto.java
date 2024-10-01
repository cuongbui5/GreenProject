package com.example.greenproject.dto.res;

import com.example.greenproject.model.enums.ItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ItemDto {
    private Long id;
    private int quantity;
    private Double totalPrice;
    private ItemStatus status;
    private ProductItemDto productItem;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
