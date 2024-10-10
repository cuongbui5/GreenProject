package com.example.greenproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/redis")
@RequiredArgsConstructor
public class RedisController {
    /*private final RedisTemplate<String, Object> redisTemplate;
    @PostMapping("/test")
    public String test(@RequestBody Object object) {
        if(Boolean.TRUE.equals(redisTemplate.hasKey("test"))){
            Object object1= redisTemplate.opsForValue().get("test");
            System.out.println(object1);
        }
        redisTemplate.opsForValue().set("test", object);
        return "ok";
    }*/
}
