package com.example.greenproject.repository;

import com.example.greenproject.model.UserVoucher;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.model.pk.UserVoucherId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherUserRepository extends JpaRepository<UserVoucher, UserVoucherId> {
}
