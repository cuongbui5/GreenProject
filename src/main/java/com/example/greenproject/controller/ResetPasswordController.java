package com.example.greenproject.controller;

import com.example.greenproject.dto.MailBody;
import com.example.greenproject.dto.req.ResetPasswordRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.ResetPasswordToken;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.ResetPasswordTokenRepository;
import com.example.greenproject.service.EmailService;
import com.example.greenproject.service.ResetPasswordService;
import com.example.greenproject.service.UserService;
import com.example.greenproject.utils.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/forgotPassword")
public class ResetPasswordController {
    private final ResetPasswordService resetPassword;

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<?> verityEmail(@Email(message = "khong dung dinh dang email") @PathVariable("email")String email){
        String result = resetPassword.verifyEmail(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse(
                        HttpStatus.OK.value(),
                        result
                ));
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<?> verifyOtp(@PathVariable("otp") Integer otp,
                                       @Email(message = "khong dung dinh dang email") @PathVariable("email") String email){
        String result = resetPassword.verifyOtp(email,otp);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse(
                        HttpStatus.OK.value(),
                        "Xac nhan OTP thanh cong"
                ));
    }

    @PutMapping("/changePassword/{email}")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
                                            @Email(message = "khong dung dinh dang email") @PathVariable("email") String email){
        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        resetPassword.resetPassword(resetPasswordRequest,email)
                )
        );
    }

}
