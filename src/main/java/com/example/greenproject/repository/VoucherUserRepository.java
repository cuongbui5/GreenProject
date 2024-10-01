package com.example.greenproject.repository;

import com.example.greenproject.model.UserVoucher;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.model.pk.UserVoucherId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherUserRepository extends JpaRepository<UserVoucher, UserVoucherId> {

    @EntityGraph(attributePaths = {"id.voucher", "id.user"})
    Page<UserVoucher> findByIdUserId(Long userId, Pageable pageable);
}
