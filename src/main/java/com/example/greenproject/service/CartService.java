package com.example.greenproject.service;

import com.example.greenproject.model.Cart;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    public Cart createCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart != null) {
            return cart;

        }
        User user=userService.getUserById(userId);
        Cart newCart=new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);


    }
    public Cart getCartByUserId(Long userId ) {
        return cartRepository.findByUserId(userId);

    }
    public Optional<Cart> getCartById(Long cartId ) {
        return cartRepository.findById(cartId);

    }


}
