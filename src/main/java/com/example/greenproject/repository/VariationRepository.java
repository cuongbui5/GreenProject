package com.example.greenproject.repository;

import com.example.greenproject.model.Variation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VariationRepository extends JpaRepository<Variation,Long> {

    List<Variation> findAllByCategoryId(Long categoryId);
    Page<Variation> findAllByCategoryId(Long categoryId, Pageable pageable);

    Optional<Variation> findByName(String name);

    Page<Variation> findByNameContainingIgnoreCase(String search, Pageable pageable);

}
