package com.example.greenproject.service;

import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Cart;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.CartRepository;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    public Cart getOrCreateCart () {
        UserInfo userInfo = Utils.getUserInfoFromContext();

        if (userInfo == null) {
            throw new RuntimeException("Please log in first!");
        }

        User user = userService.getUserById(userInfo.getId());


        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });


    }




}
