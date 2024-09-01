package com.example.greenproject.controller;

import com.example.greenproject.dto.req.LoginRequest;
import com.example.greenproject.dto.req.RegisterRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.security.SecurityUtils;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.service.AuthService;
import com.example.greenproject.utils.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping( "/register")
    public ResponseEntity<BaseResponse> register(@RequestBody @Valid RegisterRequest registerRequest){
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse(HttpStatus.CREATED.value(), Constants.REGISTER_OK)

        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        UserInfo userInfo= authService.login(loginRequest);
        SecurityUtils.setJwtToClient(userInfo);

        return ResponseEntity.ok().body(new DataResponse(HttpStatus.OK.value(), "success",userInfo));

    }


}
