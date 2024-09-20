package com.example.greenproject.repository;

import com.example.greenproject.model.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    Page<ProductItem> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
}
