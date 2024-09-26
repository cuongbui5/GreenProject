package com.example.greenproject.repository;

import com.example.greenproject.model.ProductItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    Page<ProductItem> findByProductId(Long productId, Pageable pageable);

    Page<ProductItem> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
}
