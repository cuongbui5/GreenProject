package com.example.greenproject.repository;

import com.example.greenproject.model.Order;
import com.example.greenproject.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT SUM(o.totalCost) FROM Order o WHERE o.isPaid = true")
    Double calculateTotalRevenue();
}
