package com.example.greenproject.service;

import com.example.greenproject.dto.req.ChangePasswordRequest;
import com.example.greenproject.dto.req.UpdateUserRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.UserRepository;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UploadFileService uploadFileService;
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        return user.get();
    }



    public User getUserByUserInfo() {
        UserInfo userInfo= Utils.getUserInfoFromContext();
        return userRepository
                .findByUsername(userInfo.getUsername()).
                orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public User updateUser(UpdateUserRequest updateUserRequest) {
        User user = getUserByUserInfo();
        user.setFullName(updateUserRequest.getFullName());
        user.setPhoneNumber(updateUserRequest.getNumberPhone());
        return userRepository.save(user);
    }

    public User changePassword(ChangePasswordRequest changePasswordRequest) {
        if(!Objects.equals(changePasswordRequest.getNewPassword(), changePasswordRequest.getConfirmPassword())){
            throw new RuntimeException("Mat khau xac nhan ko dung!");
        }
        User user = getUserByUserInfo();
        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())){
            throw new RuntimeException("Sai mat khau!");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        return userRepository.save(user);


    }

    public User uploadAvatar(MultipartFile file) throws IOException {
        String url=uploadFileService.uploadFile(file);
        User user = getUserByUserInfo();
        if(url.isBlank()){
            throw new RuntimeException("url is blank!");
        }
        user.setImgUrl(url);
        return userRepository.save(user);

    }

    public void updateUserPoint(User user){
        userRepository.save(user);
    }


}
