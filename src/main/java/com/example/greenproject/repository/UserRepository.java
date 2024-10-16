package com.example.greenproject.repository;

import com.example.greenproject.model.User;
import com.example.greenproject.model.Voucher;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE FUNCTION('YEAR', u.createdAt) = :year AND " +
            "(CASE " +
            "WHEN FUNCTION('MONTH', u.createdAt) IN (1, 2, 3) THEN 1 " +
            "WHEN FUNCTION('MONTH', u.createdAt) IN (4, 5, 6) THEN 2 " +
            "WHEN FUNCTION('MONTH', u.createdAt) IN (7, 8, 9) THEN 3 " +
            "WHEN FUNCTION('MONTH', u.createdAt) IN (10, 11, 12) THEN 4 " +
            "END) = :quarter")
    Long countUsersByQuarter(@Param("year") int year, @Param("quarter") int quarter);

    @Query("SELECT o.user " +
            "FROM Order o " +
            "WHERE o.updatedAt BETWEEN :startDate AND :endDate " +
            "GROUP BY o.user " +
            "ORDER BY SUM(o.totalCost) DESC")
    Page<User> findByTotalOrderValue(@Param("startDate") ZonedDateTime startDate,
                                     @Param("endDate") ZonedDateTime endDate,
                                     Pageable pageable);
}
