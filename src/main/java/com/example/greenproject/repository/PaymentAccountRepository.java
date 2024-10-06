package com.example.greenproject.repository;

import com.example.greenproject.model.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {
    List<PaymentAccount> findByUserId(Long userId);
    Optional<PaymentAccount> findByAccountNumberAndBankId(String accountNumber, Long bankId);
}
