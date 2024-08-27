package com.example.greenproject.repository;

import com.example.greenproject.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);

    Optional<Product> findByName(String name);
}
