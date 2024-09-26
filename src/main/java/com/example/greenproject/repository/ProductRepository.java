package com.example.greenproject.repository;

import com.example.greenproject.model.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Optional<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    Page<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds,Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:search% AND p.category.id IN :categoryIds")
    Page<Product> findBySearchAndCategoryIds(@Param("search") String search, @Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.productItems pi GROUP BY p.id ORDER BY SUM(pi.sold) DESC")
    Page<Product> findByTopSold(Pageable pageable);


    @Query("SELECT p FROM Product p JOIN p.productItems pi GROUP BY p.id ORDER BY MIN(pi.price) ASC")
    Page<Product> findAllProductsOrderByLowestPrice(Pageable pageable);

}
