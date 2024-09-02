package com.example.greenproject.service;

import com.example.greenproject.dto.req.LoginRequest;
import com.example.greenproject.dto.req.RegisterRequest;
import com.example.greenproject.model.Role;
import com.example.greenproject.model.User;
import com.example.greenproject.model.enums.UserType;
import com.example.greenproject.repository.RoleRepository;
import com.example.greenproject.repository.UserRepository;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Constants;
import com.example.greenproject.utils.Utils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User register(RegisterRequest registerRequest) {
        if (!registerRequest.isPasswordValid()){
            throw new RuntimeException("Mật khẩu xác nhận không đúng!");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new RuntimeException("Tài khoản đã được sử dụng!");
        }
        Optional<Role> role=roleRepository.findByName(Constants.ROLE_USER);
        HashSet<Role> roles=new HashSet<>();
        roles.add(role.get());
        User user= new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(roles);
        user.setEmail(registerRequest.getEmail());
        if(Objects.equals(registerRequest.getUserType(), UserType.GITHUB.name())){
            user.setUserType(UserType.GITHUB);
        }else if(Objects.equals(registerRequest.getUserType(), UserType.GOOGLE.name())){
            user.setUserType(UserType.GOOGLE);
        }else {
            user.setUserType(UserType.NONE);
        }

        return userRepository.save(user);




    }
    public boolean checkExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }





    public UserInfo login(LoginRequest loginRequest) throws IOException {
        String username=loginRequest.getUsername();
        Optional<User> userOptional=userRepository.findByUsername(username);
        if(userOptional.isPresent()&&passwordEncoder.matches(loginRequest.getPassword(),userOptional.get().getPassword())){
            User user=userOptional.get();
            UserInfo userInfo=new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setEmail(user.getEmail());
            userInfo.setId(user.getId());
            userInfo.setRoles(user.getRoles());

            return userInfo;

        }

        throw new RuntimeException("Username or password incorrect!");


    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Utils.removeAllCookies(request,response);

    }
}
