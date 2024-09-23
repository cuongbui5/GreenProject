package com.example.greenproject.controller;

import com.example.greenproject.dto.req.ChangePasswordRequest;
import com.example.greenproject.dto.req.UpdateUserRequest;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.service.UserService;
import com.example.greenproject.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/user-info")
    public ResponseEntity<?> userInfo() {
        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        userService.getUserInfo()
                )
        );

    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        userService.updateUser(updateUserRequest)
                )
        );

    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        userService.changePassword(changePasswordRequest)
                )
        );

    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestPart("avatar") final MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        Constants.SUCCESS_MESSAGE,
                        userService.uploadAvatar(file)
                )
        );

    }

}
