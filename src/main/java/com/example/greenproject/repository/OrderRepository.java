package com.example.greenproject.repository;

import com.example.greenproject.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"contact", "voucher", "items.productItem.product.category","items.productItem.product.images", "items.productItem.variationOptions"})
    Optional<Order> findById(Long id);

}
