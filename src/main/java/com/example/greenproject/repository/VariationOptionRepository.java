package com.example.greenproject.repository;

import com.example.greenproject.model.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VariationOptionRepository extends JpaRepository<VariationOption,Long> {
    Optional<VariationOption> findByValue(String value);

    @Query(nativeQuery = true,value = "SELECT * FROM _variation_option vo WHERE vo.variation_id =:id")
    List<VariationOption> findByVariationId(@Param("id") Long id);
}
