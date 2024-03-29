package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.service.FollowWatchingService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/follows")
@Slf4j
public class FollowWatchingController {
    @Autowired
    private UserService userService;
    @Autowired
    private FollowWatchingService followWatchingService;

    @GetMapping("/getListFollowByIdUser")
    public ResponseEntity<?> getListFollowByIdUser(@RequestParam Long idUser) {
        List<UserDTO> userList = userService.filterUser(idUser, Constants.FollowPeople.FOLLOW);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/getListWatchingByIdUser")
    public ResponseEntity<?> getListWatchingByIdUser(@RequestParam Long idUser) {
        List<UserDTO> userList = userService.filterUser(idUser, Constants.FollowPeople.WATCHING);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // Theo dõi
    @GetMapping("/follow")
    public ResponseEntity<?> createFollow(@RequestParam Long idUserLogin, @RequestParam Long idUserFollow) {
        followWatchingService.createFollow(idUserLogin, idUserFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Bỏ theo dõi
    @GetMapping("/unFollow")
    public ResponseEntity<?> unFollow(@RequestParam Long idUserLogin, @RequestParam Long idUserFollow) {
        followWatchingService.unFollow(idUserLogin, idUserFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getOne")
    public ResponseEntity<?> getOne(@RequestParam Long idUserLogin, @RequestParam Long idUserFollow) {
        Object user = followWatchingService.checkUserHadFollow(idUserLogin, idUserFollow);
        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
