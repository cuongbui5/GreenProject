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
        config.put("cloud_name", "dvpx0s59u");
        config.put("api_key", "314678288715114");
        config.put("api_secret", "V_rcUdVdOfJRUts3kBZTnL4KlMg");
        return new Cloudinary(config);
    }
}
