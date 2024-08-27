package com.example.greenproject.repository;

import com.example.greenproject.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Dictionary;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsCartByUserId(Long userId);

    Cart findByUserId(Long userId);
}
