package com.example.greenproject.dto.req;

import lombok.Data;

@Data
public class LinkPaymentAccount {
    private Long bankId;
    private String accountNumber;
    private String fullName;
    private String pinCode
            ;
}
