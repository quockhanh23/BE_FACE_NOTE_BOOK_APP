package com.example.final_case_social_web.controller;


import com.example.final_case_social_web.model.Post2;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.service.PostService;
import com.example.final_case_social_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "api/redis")
public class RedisController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/getAllPost")
    public ResponseEntity<?> redisGetAllPost() {
        Iterable<Post2> iterable = postService.findAll();
        return new ResponseEntity<>(iterable, HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<?> redisGetAllUser() {
        Iterable<User> iterable = userService.findAll();
        return new ResponseEntity<>(iterable, HttpStatus.OK);
    }
}
