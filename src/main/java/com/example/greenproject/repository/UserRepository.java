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

    @Query("SELECT COUNT(u) FROM User u WHERE FUNCTION('YEAR', u.createdAt) = :year AND FUNCTION('MONTH', u.createdAt) = :month")
    Long countUsersByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT o.user " +
            "FROM Order o " +
            "GROUP BY o.user " +
            "ORDER BY SUM(o.totalCost) DESC")
    List<User> findTopUsersByTotalOrderValue();
}
