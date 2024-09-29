package com.example.greenproject.repository;

import com.example.greenproject.model.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {


    @EntityGraph(attributePaths = {"product"})
    Page<ProductItem> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"product"})
    Page<ProductItem> findByProductId(Long productId, Pageable pageable);

    Page<ProductItem> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
}
