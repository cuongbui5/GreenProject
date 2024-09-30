package com.example.greenproject.dto.res;

import com.example.greenproject.model.enums.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private Long id;
    private int quantity;
    private Double totalPrice;
    private ItemStatus status;
    private ProductItemDtoDetail productItemDtoDetail;
}
