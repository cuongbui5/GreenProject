package com.example.greenproject.repository;

import com.example.greenproject.dto.res.ProductDtoView;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDtoViewRepository extends JpaRepository<ProductDtoView, Long> {

    Page<ProductDtoView> findAll(Pageable pageable);
    Page<ProductDtoView> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "SELECT * FROM product_dto_view psv ORDER BY psv.sold DESC",nativeQuery = true)
    Page<ProductDtoView> findByTopSold(Pageable pageable);

    @Query(value = "SELECT * FROM product_dto_view psv ORDER BY psv.min_price ASC",nativeQuery = true)
    Page<ProductDtoView> findAllProductsOrderByLowestPrice(Pageable pageable);

    @Query(value = "SELECT * FROM product_dto_view psv WHERE psv.category_id IN :categoryIds",nativeQuery = true)
    Page<ProductDtoView> findByCategoryId(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);
}
