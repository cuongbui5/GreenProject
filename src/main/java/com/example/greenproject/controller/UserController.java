package com.example.greenproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/user-infor")
    public Map<String, Object> userInfor(@AuthenticationPrincipal OAuth2User user) {
        System.out.println(user.getAttributes());
        return user.getAttributes();
    }
}
