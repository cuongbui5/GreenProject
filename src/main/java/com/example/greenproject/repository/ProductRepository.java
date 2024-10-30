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


    @Query("SELECT p.id, " +
            "       p.name, " +
            "       MIN(pi.price) , " +
            "       MAX(pi.price) , " +
            "       SUM(i.quantity) , " +
            "       SUM(pi.totalRating) , " +
            "       COUNT(pi.reviewsCount) " +
            "FROM Item i " +
            "JOIN i.productItem pi " +
            "JOIN pi.product p " +
            "JOIN i.order o " +
            "WHERE FUNCTION('YEAR', o.updatedAt) = :year " +
            "AND (CASE " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (1, 2, 3) THEN 1 " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (4, 5, 6) THEN 2 " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (7, 8, 9) THEN 3 " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (10, 11, 12) THEN 4 " +
            "     END) = :quarter " +
            "GROUP BY p.id " +
            "ORDER BY SUM(i.quantity) DESC")
    Page<Object> findTopSellingProductByQuarter(@Param("year") int year,
                                                @Param("quarter") int quarter,
                                                Pageable pageable);

    /*@Query("SELECT p.id, " +
            "       p.name, " +
            "       MIN(pi.price) , " +
            "       MAX(pi.price) , " +
            "       SUM(i.quantity) , " +
            "       SUM(pi.totalRating) , " +
            "       COUNT(pi.reviewsCount) " +
            "FROM Item i " +
            "JOIN i.productItem pi " +
            "JOIN pi.product p " +
            "JOIN i.order o " +
            "WHERE FUNCTION('YEAR', o.updatedAt) = :year " +
            "AND (CASE " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (1, 2, 3) THEN 1 " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (4, 5, 6) THEN 2 " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (7, 8, 9) THEN 3 " +
            "        WHEN FUNCTION('MONTH', o.updatedAt) IN (10, 11, 12) THEN 4 " +
            "     END) = :quarter " +
            "GROUP BY p.id " +
            "ORDER BY SUM(i.quantity) DESC")

     */
}
