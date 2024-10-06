package com.example.greenproject.repository;

import com.example.greenproject.model.Voucher;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Page<Voucher> findByNameContainingIgnoreCase(String search, Pageable pageable);
    @Query("SELECT v FROM Voucher v JOIN v.users u WHERE u.id = :userId")
    Page<Voucher> findAllByUserId(@Param("userId") Long userId,Pageable pageable);

    List<Voucher> findByName(String name);

    @Query(value = "SELECT * FROM _voucher v WHERE v.quantity > 0 " +
            "AND v.start_date <= :now " +
            "AND v.end_date >= :now",
            countQuery = "SELECT COUNT(*) FROM _voucher v WHERE v.quantity > 0 " +
                    "AND v.start_date <= :now " +
                    "AND v.end_date >= :now",
            nativeQuery = true)
    Page<Voucher> findValidVouchers(@Param("now") ZonedDateTime now,Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM _user_voucher WHERE user_id = :userId AND voucher_id = :voucherId", nativeQuery = true)
    void deleteUserVoucher(Long userId, Long voucherId);


}
