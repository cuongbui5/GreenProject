package com.example.greenproject.repository;

import com.example.greenproject.model.VariationOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VariationOptionRepository extends JpaRepository<VariationOption,Long> {
    Optional<VariationOption> findByValue(String value);
    Page<VariationOption> findByVariationId(Long id, Pageable pageable);

    Page<VariationOption> findByValueContainingIgnoreCase(String search, Pageable pageable);
}
