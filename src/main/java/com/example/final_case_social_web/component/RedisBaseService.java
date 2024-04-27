package com.example.final_case_social_web.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisBaseService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Lưu giá trị vào Redis với key và value
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Lấy giá trị từ Redis với key
    public Object getObjectByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Xóa key khỏi Redis
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}
