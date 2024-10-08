package com.example.greenproject.repository;

import com.example.greenproject.model.Product;
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
public interface ProductRepository extends JpaRepository<Product,Long> {
    @EntityGraph(attributePaths = {"category"})
    List<Product> findAll();
    Optional<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    Page<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds,Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:search% AND p.category.id IN :categoryIds")
    Page<Product> findBySearchAndCategoryIds(@Param("search") String search, @Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "images"})
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByProductId(Long productId);
}
