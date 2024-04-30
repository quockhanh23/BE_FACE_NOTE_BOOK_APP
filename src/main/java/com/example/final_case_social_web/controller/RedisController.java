package com.example.final_case_social_web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "api/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/getAllKeys")
    public ResponseEntity<?> getAllKeys() {
        Set<String> getAllKeys = redisTemplate.keys("*");
        return new ResponseEntity<>(getAllKeys, HttpStatus.OK);
    }
}
