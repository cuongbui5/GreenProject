package com.example.greenproject.repository;

import com.example.greenproject.model.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VariationRepository extends JpaRepository<Variation,Long> {

    @Query(nativeQuery = true,value = "SELECT * FROM _variation v JOIN _variation_category vc ON v.id = vc.variation_id WHERE vc.category_id =:id")
    List<Variation> getAllVariationByCategoryId(@Param("id")Long id);

    Optional<Variation> findByName(String name);
}
