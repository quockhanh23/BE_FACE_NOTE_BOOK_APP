package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.model.UserDescription;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.UserDescriptionService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/descriptions")
@Slf4j
public class UserDescriptionRestController {
    @Autowired
    private UserDescriptionService userDescriptionService;
    @Autowired
    private UserService userService;

    @GetMapping("/getDescriptionByIdUser")
    public ResponseEntity<?> getDescriptionByIdUser(@RequestParam Long idUser) {
        List<UserDescription> list = userDescriptionService.findAllByUserId(idUser);
        if (CollectionUtils.isEmpty(list)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(list.get(0), HttpStatus.OK);
    }

    // Tạo mới mô tả
    @PostMapping("/createDescription")
    public ResponseEntity<?> createDescription(@RequestBody UserDescription userDescription,
                                               @RequestParam Long idUser) {
        if (StringUtils.isEmpty(userDescription.getDescription().trim())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Common.handlerWordsLanguage(userDescription);
        Optional<User> optionalUser = userService.findById(idUser);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        userDescription.setUser(optionalUser.get());
        userDescription.setCreateAt(new Date());
        userDescription.setStatus(Constants.STATUS_PUBLIC);
        userDescriptionService.save(userDescription);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Sửa mô tả
    @PutMapping("/editDescription")
    public ResponseEntity<?> editDescription(@RequestBody UserDescription userDescription,
                                             @RequestParam Long idUserDescription) {
        if (StringUtils.isEmpty(userDescription.getDescription().trim())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Common.handlerWordsLanguage(userDescription);
        Optional<UserDescription> description = userDescriptionService.findById(idUserDescription);
        if (!description.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER_DESCRIPTION, idUserDescription), HttpStatus.NOT_FOUND);
        }
        description.get().setEditAt(new Date());
        description.get().setDescription(userDescription.getDescription());
        userDescriptionService.save(description.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Xóa mô tả
    @DeleteMapping("/deleteDescription")
    public ResponseEntity<?> deleteDescription(@RequestParam Long idUser) {
        List<UserDescription> list = userDescriptionService.findAllByUserId(idUser);
        if (!CollectionUtils.isEmpty(list)) userDescriptionService.delete(list.get(0));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
