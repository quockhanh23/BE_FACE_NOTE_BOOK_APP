package com.example.final_case_social_web.controllertest;


import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllKeys")
    public ResponseEntity<?> getAllKeys() {
        Set<String> getAllKeys = redisTemplate.keys("*");
        return new ResponseEntity<>(getAllKeys, HttpStatus.OK);
    }

    @GetMapping("/pushObject")
    public ResponseEntity<?> pushObject() {
        List<User> userList = userService.findAllRoleUser();
        List<UserDTO> userDTOList = userService.copyListDTO(userList);
        redisTemplate.opsForList().rightPushAll("listUser", userDTOList, Duration.ofSeconds(60));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pushObject1")
    public ResponseEntity<?> pushObject1() {
        List<User> userList = userService.findAllRoleUser();
        List<UserDTO> userDTOList = userService.copyListDTO(userList);
        redisTemplate.opsForList().rightPushAll("listUser1", userDTOList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getObject")
    public ResponseEntity<?> getObject() {
        List<?> objectList = redisTemplate.opsForList().range("listUser", 0, -1);
        return new ResponseEntity<>(objectList, HttpStatus.OK);
    }

    @GetMapping("/getObject1")
    public ResponseEntity<?> getObject2() {
        List<?> objectList = redisTemplate.opsForList().range("listUser1", 0, -1);
        return new ResponseEntity<>(objectList, HttpStatus.OK);
    }

    @GetMapping("/left-push")
    public ResponseEntity<?> leftPush() {
        Long push = redisTemplate.opsForList().leftPush("test", "done");
        return new ResponseEntity<>(push, HttpStatus.OK);
    }

    @GetMapping("/getAllKeysString")
    public ResponseEntity<?> StringRedisTemplate() {
        Set<String> getAllKeys = stringRedisTemplate.keys("*");
        return new ResponseEntity<>(getAllKeys, HttpStatus.OK);
    }

    @GetMapping("/pushString")
    public ResponseEntity<?> pushString() {
        List<User> userList = userService.findAllRoleUser();
        List<UserDTO> userDTOList = userService.copyListDTO(userList);
        try {
            ObjectMapper objectMapper = Common.intObjectMapper();
            String json = objectMapper.writeValueAsString(userDTOList);
            stringRedisTemplate.opsForValue().set("test2", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getValue")
    public ResponseEntity<?> getValue() {
        String getData = stringRedisTemplate.opsForValue().get("test2");
        try {
            ObjectMapper objectMapper = Common.intObjectMapper();
            UserDTO[] myObjects = objectMapper.readValue(getData, UserDTO[].class);
            return new ResponseEntity<>(myObjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/stringRedis")
    public ResponseEntity<?> stringRedis(@RequestParam String key) {
        stringRedisTemplate.delete(key);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/objectRedis")
    public ResponseEntity<?> objectRedis(@RequestParam String key) {
        redisTemplate.delete(key);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
