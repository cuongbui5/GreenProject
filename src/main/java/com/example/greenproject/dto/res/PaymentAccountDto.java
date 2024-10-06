package com.example.greenproject.dto.res;

import com.example.greenproject.model.Bank;
import com.example.greenproject.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class PaymentAccountDto {
    private Long id;
    private String accountNumber;
    private String fullName;
    private Double balance;
    private Bank bank;

}
