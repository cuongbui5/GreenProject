package com.example.greenproject.repository;

import com.example.greenproject.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher,Long> {

    Page<Voucher> findByNameContainingIgnoreCase(@Param("name")String name, Pageable pageable);

    List<Voucher> findByName(@Param("name") String name);
}
