package com.example.greenproject.dto.req;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CreateCartItemRequest {
    private Long productItemId;
    private int quantity;

}
