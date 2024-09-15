package com.example.greenproject.repository;

import com.example.greenproject.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem,Long> {

}
