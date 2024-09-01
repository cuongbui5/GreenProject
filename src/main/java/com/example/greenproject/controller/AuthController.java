package com.example.greenproject.controller;

import com.example.greenproject.dto.req.LoginRequest;
import com.example.greenproject.dto.req.RegisterRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.LoginResponse;
import com.example.greenproject.model.User;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.service.AuthService;
import com.example.greenproject.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
        authService.login(loginRequest);
        return ResponseEntity.ok()
                .body(new BaseResponse(HttpStatus.OK.value(), "Đăng nhập thành công!"));
    }
}
