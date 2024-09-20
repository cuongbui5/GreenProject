package com.example.greenproject.repository;

import com.example.greenproject.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Page<Voucher> findByNameContainingIgnoreCase(String search, Pageable pageable);

    List<Voucher> findByName(String name);
}
