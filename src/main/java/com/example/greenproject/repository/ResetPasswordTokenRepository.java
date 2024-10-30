package com.example.greenproject.repository;

import com.example.greenproject.model.ResetPasswordToken;
import com.example.greenproject.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken,Long> {

    @Query("SELECT rpt FROM ResetPasswordToken rpt WHERE rpt.otp =:otp AND rpt.user =:user")
    Optional<ResetPasswordToken> findByOtpAndUser(@Param("otp") Integer otp, @Param("user")User user);

    @Query("SELECT rpt FROM ResetPasswordToken rpt WHERE rpt.user =:user")
    Optional<ResetPasswordToken> findByUser(@Param("user")User user);
}
