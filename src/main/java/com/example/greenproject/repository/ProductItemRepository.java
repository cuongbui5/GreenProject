package com.example.greenproject.repository;

import com.example.greenproject.model.ProductItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    @EntityGraph(attributePaths = {"product"})
    Page<ProductItem> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"product"})
    Page<ProductItem> findByProductId(Long productId, Pageable pageable);
    @EntityGraph(attributePaths = {"variationOptions.variation"})
    List<ProductItem> findByProductId(Long productId);
    @Query("SELECT pi FROM ProductItem pi WHERE pi.id = :id")
    Optional<ProductItem> findById(@Param("id") Long id);

    Page<ProductItem> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
}
