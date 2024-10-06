package com.example.greenproject.service;

import com.example.greenproject.model.Bank;
import com.example.greenproject.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    public List<Bank> findAll() {
        return bankRepository.findAll();
    }
}
