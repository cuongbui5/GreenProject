package com.example.greenproject.service;

import com.example.greenproject.dto.MailBody;
import com.example.greenproject.dto.req.ResetPasswordRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.model.ResetPasswordToken;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.ResetPasswordTokenRepository;
import com.example.greenproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public String verifyEmail(String email){
        User user = userRepository
                .findByEmail(email).orElseThrow(()-> new RuntimeException("Email khong ton tai trong du lieu cua chung toi"));

        Optional<ResetPasswordToken> resetPasswordTokenOptional = resetPasswordTokenRepository.findByUser(user);

        resetPasswordTokenOptional.ifPresent(resetPasswordToken -> resetPasswordTokenRepository.deleteById(resetPasswordToken.getId()));

        int otp = otpGenerate();
        MailBody mailBody = MailBody
                .builder()
                .to(email)
                .text("Day la OTP de thay doi mat khau " + otp)
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

        return "OTP da gui den email cua ban!";
    }

    public String verifyOtp(String email, Integer otp){
        User user = userRepository
                .findByEmail(email).orElseThrow(()-> new RuntimeException("Email khong ton tai trong du lieu cua chung toi"));

        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository
                .findByOtpAndUser(otp, user).orElseThrow(()-> new RuntimeException("OTP khong dung cho email"));

        if(resetPasswordToken.getExpirationTime().before(Date.from(Instant.now()))){
            resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());
            throw new RuntimeException("OTP da het han");
        }

        return "OTP da duoc xac nhan thanh cong!";
    }

    public String resetPassword(ResetPasswordRequest resetPasswordRequest, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Khong tim thay email!"));

        String newPassword = resetPasswordRequest.getNewPassword();
        String confirmPassword = resetPasswordRequest.getConfirmPassword();
        if(!Objects.equals(newPassword,confirmPassword)){
            throw new RuntimeException("Mat khau khong hop le, vui long nhap lai mat khau");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return "Mat khau thay doi thanh cong";
    }

    private Integer otpGenerate(){
        return new Random().nextInt(100_000,999_999);
    }

}
