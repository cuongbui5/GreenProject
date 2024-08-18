package com.example.greenproject.controller;

import com.example.greenproject.security.SecurityUtils;
import com.example.greenproject.security.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/business")
    String business(){
        var session = SecurityUtils.getSession();
        return "Business " + session;
    }
}
