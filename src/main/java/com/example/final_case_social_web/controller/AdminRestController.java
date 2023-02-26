package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.Post2;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.PostService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/admins")
@Slf4j
public class AdminRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    // Xem tất cả user
    @GetMapping("/adminAction")
    public ResponseEntity<?> adminAction(@RequestParam Long idAdmin,
                                        @RequestParam String type,
                                        @RequestHeader("Authorization") String authorization) {
        if (userService.checkAdmin(idAdmin) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idAdmin), HttpStatus.UNAUTHORIZED);
        }
        ResponseEntity<?> responseEntity = userService.errorToken(authorization, idAdmin);
        if (responseEntity != null) {
            return responseEntity;
        }
        if ("user".equals(type)) {
            List<User> users = userService.findAllRoleUser();
            List<UserDTO> userDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(users)) {
                for (User user : users) {
                    UserDTO userDTO = new UserDTO();
                    BeanUtils.copyProperties(user, userDTO);
                    userDTOList.add(userDTO);
                }
            }
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        }
        if ("post".equals(type)) {
            Iterable<Post2> post2List = postService.findAll();
            List<Post2> list = (List<Post2>) post2List;
            list.sort((p1, p2) -> (p2.getCreateAt().compareTo(p1.getCreateAt())));
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    // Cấm user, Kích hoạt tài khoản
    @DeleteMapping("/actionUser")
    public ResponseEntity<?> actionUser(@RequestParam Long idAdmin,
                                                 @RequestParam Long idUser,
                                                 @RequestParam String type,
                                                 @RequestHeader("Authorization") String authorization) {
        if (userService.checkAdmin(idAdmin) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idAdmin), HttpStatus.UNAUTHORIZED);
        }
        ResponseEntity<?> responseEntity = userService.errorToken(authorization, idAdmin);
        if (responseEntity != null) {
            return responseEntity;
        }
        Optional<User> optionalUser = userService.findById(idUser);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        if (userService.checkAdmin(idAdmin).toString().substring(17, 27).equals(Constants.Roles.ROLE_ADMIN)) {
            if ("baned".equals(type)) {
                optionalUser.get().setStatus(Constants.STATUS_BANED);
            }
            if ("active".equals(type)) {
                optionalUser.get().setStatus(Constants.STATUS_ACTIVE);
            }
            userService.save(optionalUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Xoá bài viết trong database, khóa bài viết
    @DeleteMapping("/actionPost")
    public ResponseEntity<?> actionPost(@RequestParam Long idAdmin,
                                        @RequestParam Long idPost,
                                        @RequestParam String type,
                                        @RequestHeader("Authorization") String authorization) {
        if (userService.checkAdmin(idAdmin) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idAdmin), HttpStatus.UNAUTHORIZED);
        }
        ResponseEntity<?> responseEntity = userService.errorToken(authorization, idAdmin);
        if (responseEntity != null) {
            return responseEntity;
        }
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
        }
        if (userService.checkAdmin(idAdmin).toString().substring(17, 27).equals(Constants.Roles.ROLE_ADMIN)) {
            if ("delete".equals(type)) {
                postService.delete(postOptional.get());
            }
            if ("lock".equals(type)) {
                postOptional.get().setStatus(Constants.STATUS_PRIVATE);
                postService.save(postOptional.get());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }
}
