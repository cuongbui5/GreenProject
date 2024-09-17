package com.example.greenproject.init;

import com.example.greenproject.model.Role;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.RoleRepository;
import com.example.greenproject.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RoleInitializer {
    @Bean
    public CommandLineRunner initializeRoles(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Tạo role USER nếu chưa tồn tại
            if (roleRepository.findByName("USER").isEmpty()) {
                Role role = new Role();
                role.setName("USER");
                roleRepository.save(role);
            }



            // Tạo role ADMIN nếu chưa tồn tại
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role role = new Role();
                role.setName("ADMIN");
                roleRepository.save(role);
            }
        };
    }
}
