package com.example.greenproject.dto.req;

import com.example.greenproject.model.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Size(min = 4, message = "Tài khoản phải có ít nhất 4 kí tự")
    private String username;
    @Email
    private String email;
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 kí tự")
    private String password;
    private String passwordConfirm;
    private String userType;

    public boolean isPasswordValid() {
        return password.equals(passwordConfirm);
    }


}
