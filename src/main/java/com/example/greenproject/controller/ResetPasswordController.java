package com.example.greenproject.controller;

import com.example.greenproject.dto.MailBody;
import com.example.greenproject.dto.req.ChangePasswordRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.ResetPasswordToken;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.ResetPasswordTokenRepository;
import com.example.greenproject.service.EmailService;
import com.example.greenproject.service.UserService;
import com.example.greenproject.utils.Constants;
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
@RequestMapping("/forgotPassword")
public class ResetPasswordController {
    private final UserService userService;
    private final EmailService emailService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<?> verityEmail(@PathVariable("email")String email){
        User user = userService
                .getUserByEmail(email);

        Optional<ResetPasswordToken> resetPasswordTokenOptional = resetPasswordTokenRepository.findByUser(user);

        if(resetPasswordTokenOptional.isPresent()){
            resetPasswordTokenRepository.deleteById(resetPasswordTokenOptional.get().getId());
        }

        int otp = otpGenerate();
        MailBody mailBody = MailBody
                .builder()
                .to(email)
                .text("Day la OTP de thay doi mat khau" + otp)
                .subject("OTP thay doi mat khau")
                .build();

        ResetPasswordToken resetPasswordToken = ResetPasswordToken
                .builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        emailService.sendEmailMessage(mailBody);
        resetPasswordTokenRepository.save(resetPasswordToken);
        return ResponseEntity.status(HttpStatus.OK).body("Email send for verification");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<?> verifyOtp(@PathVariable("otp") Integer otp, @PathVariable("email") String email){
        User user = userService
                .getUserByEmail(email);

        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository
                .findByOtpAndUser(otp, user).orElseThrow(()-> new RuntimeException("OTP khong dung cho email"));

        if(resetPasswordToken.getExpirationTime().before(Date.from(Instant.now()))){
            resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Token da het han");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Xac nhan Token thanh cong");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        userService.changePassword(changePasswordRequest)
                )
        );
    }


    private Integer otpGenerate(){
        return new Random().nextInt(100_000,999_999);
    }
}
