package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.UserBlackListDTO;
import com.example.final_case_social_web.model.BlackList;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.BlackListService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/blackLists")
@Slf4j
public class BlackListController {
    @Autowired
    private BlackListService blackListService;
    @Autowired
    private UserService userService;

    // Danh sách những người đã chặn
    @GetMapping("/listBlockedByUser")
    public ResponseEntity<?> listBlockedByUser(@RequestParam Long idUserLogin) {
        List<BlackList> blackLists = blackListService.listBlockedByIdUser(idUserLogin);
        List<UserBlackListDTO> userBlackListDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(blackLists)) {
            List<Long> listIdUser = blackLists.stream().map(BlackList::getIdUserOnTheList).collect(Collectors.toList());
            List<User> userList = userService.findAllByIdIn(listIdUser);
            userList.forEach(user -> {
                UserBlackListDTO userBlackListDTO = new UserBlackListDTO();
                BeanUtils.copyProperties(user, userBlackListDTO);
                userBlackListDTOS.add(userBlackListDTO);
            });
        }
        return new ResponseEntity<>(userBlackListDTOS, HttpStatus.OK);
    }

    // Chặn 1 người
    @DeleteMapping("/block")
    public ResponseEntity<?> block(@RequestParam Long idUserLogin, @RequestParam Long idUserBlock) {
        try {
            Optional<User> userLogin = this.userService.findById(idUserLogin);
            if (!userLogin.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_USER, idUserLogin), HttpStatus.NOT_FOUND);
            }
            Optional<User> userBlock = this.userService.findById(idUserBlock);
            if (!userBlock.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_USER, idUserLogin), HttpStatus.NOT_FOUND);
            }
            Optional<BlackList> blackListOptional = blackListService.findBlock(userLogin.get().getId(), userBlock.get().getId());
            if (!blackListOptional.isPresent()) {
                BlackList blackList = new BlackList();
                blackListService.createDefault(blackList, idUserLogin, idUserBlock, Constants.CREATE);
            } else {
                blackListOptional.get().setEditAt(new Date());
                blackListOptional.get().setStatus(Constants.BLOCKED);
                blackListService.create(blackListOptional.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Bỏ chặn
    @DeleteMapping("/unBlock")
    public ResponseEntity<?> unBlock(@RequestParam Long idUserLogin, @RequestParam Long idUserBlock) {
        try {
            Optional<User> userOptional = this.userService.findById(idUserLogin);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_USER, idUserLogin), HttpStatus.NOT_FOUND);
            }
            Optional<BlackList> blackListOptional = blackListService.findBlock(idUserLogin, idUserBlock);
            if (blackListOptional.isPresent()) {
                if (blackListOptional.get().getStatus().equals(Constants.BLOCKED)) {
                    blackListService.createDefault(blackListOptional.get(), idUserLogin, 0L, Constants.UPDATE);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
