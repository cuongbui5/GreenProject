package com.example.greenproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class LazySecurityContextProviderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();

        System.out.println("Request URI: " + path);
        System.out.println("Request URL: " + requestUrl);
        System.out.println("Request Method: " + method);


        if (path.contains("/api/auth")||path.contains("/api/usr-info")) {
            filterChain.doFilter(request, response);
            return;
        }
        var context = SecurityContextHolder.getContext();
        SecurityContextHolder.setContext(new LazyJwtSecurityContextProvider(request, response, context));
        filterChain.doFilter(request, response);
    }
}
