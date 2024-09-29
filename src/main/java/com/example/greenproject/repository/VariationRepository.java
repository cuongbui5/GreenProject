package com.example.greenproject.repository;


import com.example.greenproject.model.Variation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface VariationRepository extends JpaRepository<Variation,Long> {
    @EntityGraph(attributePaths = {"category"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Variation> findAll();
    @EntityGraph(attributePaths = {"variationOptions"})
    List<Variation> findAllByCategoryId(Long categoryId);
    Page<Variation> findAllByCategoryId(Long categoryId, Pageable pageable);
    Page<Variation> findByNameContainingIgnoreCase(String search, Pageable pageable);

}
