package com.example.greenproject.repository;

import com.example.greenproject.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher,Long> {

//    Optional<Voucher> findByName(@Param("name") String name);

    List<Voucher> findByName(@Param("name") String name);
}
