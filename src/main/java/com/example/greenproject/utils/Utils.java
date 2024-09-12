package com.example.greenproject.utils;

import com.example.greenproject.model.User;
import com.example.greenproject.security.UserInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.naming.Context;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {
    public static UserInfo getUserInfoFromContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (UserInfo) authentication.getPrincipal();
    }

    public static void removeAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                System.out.println("Cookie Name: " + cookieName);
                System.out.println("Cookie Value: " + cookieValue);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);


            }
        }
    }
    public static Date zonedDateTimeToDate(String zonedDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(zonedDateTimeStr, formatter);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }
}
