package com.example.greenproject.controller;

import com.example.greenproject.model.Product;
import com.example.greenproject.security.SecurityUtils;
import com.example.greenproject.security.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {

    @GetMapping
    String signing(){
        List<String> authorities = new ArrayList<>();
        authorities.add("ADMIN");

        var userDetails = new UserInfo();
        userDetails.setUsername("admin");
        userDetails.setId(1L);
        userDetails.setAuthorities(authorities);
        SecurityUtils.setJwtToClient(userDetails);
        return "signed";
    }

    @GetMapping("/test")
    public ResponseEntity<List<Product>> test() {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i <= 10000; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setName("Product " + i);
            product.setDescription("Description for product " + i);
            products.add(product);
        }

        return ResponseEntity.ok(products);
    }


    @GetMapping("/secured")
    String secured(){
        var session = SecurityUtils.getSession();
        return "Secured " + session;
    }

    @GetMapping("/admin")
    String admin(){
        System.out.println("hello");
        var session = SecurityUtils.getSession();
        return "Admin " + session;
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/USER")
    String guest(){
        var session = SecurityUtils.getSession();
        return "Guest " + session;
    }



}
