package com.example.greenproject.model;

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
    private String pin;
    private Double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id",referencedColumnName = "id")
    private Bank bank;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

}
