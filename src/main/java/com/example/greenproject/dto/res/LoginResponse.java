package com.example.greenproject.dto.res;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private String email;
    private String role;

}