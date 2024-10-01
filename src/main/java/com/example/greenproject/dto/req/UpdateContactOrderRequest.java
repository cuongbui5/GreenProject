package com.example.greenproject.dto.req;

import lombok.Data;

@Data
public class UpdateContactOrderRequest {
    private Long orderId;
    private Long contactId;
}
