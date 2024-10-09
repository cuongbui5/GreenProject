package com.example.greenproject.repository;

import com.example.greenproject.model.Order;
import com.example.greenproject.model.enums.OrderStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"contact", "voucher", "items.productItem.product.category","items.productItem.product.images", "items.productItem.variationOptions"})
    Optional<Order> findById(Long id);
    @EntityGraph(attributePaths = {"contact", "voucher", "items.productItem.product.category","items.productItem.product.images", "items.productItem.variationOptions"})
    List<Order> findByStatus(OrderStatus status, Sort sort);
    @EntityGraph(attributePaths = {"contact", "voucher", "items.productItem.product.category","items.productItem.product.images", "items.productItem.variationOptions"})
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /*------------------Thống kê số lượng order--------------------*/
    long countByIsPaidTrue();


    /*----------Thong ke so luong order theo orderStatus-----------*/
    @Query("SELECT COUNT(o) FROM Order o " +
            "WHERE (o.status =:orderStatus) " +
            "AND (o.updatedAt BETWEEN :startDate AND :endDate)")
    int countByStatusAndDateRange(@Param("orderStatus") OrderStatus orderStatus,
                       @Param("startDate") ZonedDateTime startDate,
                       @Param("endDate") ZonedDateTime endDate);

    /*------------------Tinh tong doanh thu order -----------------*/
    @Query("SELECT SUM(o.totalCost) FROM Order o WHERE (o.status != INIT) AND (o.updatedAt BETWEEN :startDate AND :endDate)")
    Double calculateTotalRevenue(@Param("startDate") ZonedDateTime startDate,
                                 @Param("endDate") ZonedDateTime endDate);
}
