package com.example.greenproject.utils;

import com.example.greenproject.model.User;
import com.example.greenproject.security.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.naming.Context;

public class Utils {
    public static UserInfo getUserInfoFromContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (UserInfo) authentication.getPrincipal();
    }
}
