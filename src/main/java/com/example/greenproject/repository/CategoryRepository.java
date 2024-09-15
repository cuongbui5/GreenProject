package com.example.greenproject.repository;

import com.example.greenproject.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByNameIgnoreCase(String name);

    //List<Category> findCategoriesByParentId(Long id);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Category> getCategoriesByParentIsNull();
    int countAllByNameIgnoreCaseAndIdNot(String name, Long id);
}
