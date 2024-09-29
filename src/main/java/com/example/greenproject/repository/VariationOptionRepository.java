package com.example.greenproject.repository;

import com.example.greenproject.model.VariationOption;
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
public interface VariationOptionRepository extends JpaRepository<VariationOption,Long> {

    @EntityGraph(attributePaths = {"variation"})
    Page<VariationOption> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"variation"})
    @Query("SELECT vo FROM VariationOption vo JOIN vo.productItems pi WHERE pi.id = :productItemId")
    List<VariationOption> findByProductItemId(@Param("productItemId") Long productItemId);

    Page<VariationOption> findByVariationId(Long id, Pageable pageable);

    Page<VariationOption> findByValueContainingIgnoreCase(String search, Pageable pageable);
}
