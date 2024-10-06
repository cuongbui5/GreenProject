package com.example.greenproject.dto.res;

import com.example.greenproject.model.Bank;
import lombok.Data;

@Data
public class PaymentAccountDto {
    private Long id;
    private String accountNumber;
    private String fullName;
    private Double balance;
    private Bank bank;

}
