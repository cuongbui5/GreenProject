package com.example.greenproject.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudConfig {
    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dji65jgy3");
        config.put("api_key", "148824877898853");
        config.put("api_secret", "INfnEX-Tw9yHEpeISrB8eQoi9YQ");
        return new Cloudinary(config);
    }
}
