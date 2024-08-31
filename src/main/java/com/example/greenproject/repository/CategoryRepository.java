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
    Optional<Category> findByName(String name);


    @Query(nativeQuery = true,value = "SELECT * FROM _category c WHERE c.parent_id = :id")
    List<Category> findByParentId(@Param("id") Long id);

    List<Category> getCategoriesByParentIsNull();
}
