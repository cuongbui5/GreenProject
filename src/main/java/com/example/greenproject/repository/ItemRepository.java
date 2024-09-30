package com.example.greenproject.repository;

import com.example.greenproject.model.Cart;
import com.example.greenproject.model.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
//    @EntityGraph(attributePaths = {"productItem"})
//    List<Item> findByCart(Cart cart);


    @EntityGraph(attributePaths = {"productItem.product.category", "productItem.product.images", "productItem.variationOptions"})
    List<Item> findByCart(Cart cart);
}
