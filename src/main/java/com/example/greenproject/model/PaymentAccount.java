package com.example.greenproject.model;

import com.example.greenproject.dto.res.PaymentAccountDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_payment_account",  uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountNumber"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String fullName;
    @JsonIgnore
    private String pinCode;
    private Double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id",referencedColumnName = "id")
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonIgnore
    private User user;

    public PaymentAccountDto mapToPaymentAccountDto() {
        PaymentAccountDto paymentAccountDto = new PaymentAccountDto();
        paymentAccountDto.setAccountNumber(accountNumber);
        paymentAccountDto.setFullName(fullName);
        paymentAccountDto.setBank(bank);
        paymentAccountDto.setBalance(balance);
        return paymentAccountDto;
    }

}
