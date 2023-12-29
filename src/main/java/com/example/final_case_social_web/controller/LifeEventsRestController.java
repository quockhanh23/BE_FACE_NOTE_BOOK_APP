package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.LifeEvents;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.LifeEventsService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/events")
@Slf4j
public class LifeEventsRestController {
    @Autowired
    private LifeEventsService lifeEventsService;
    @Autowired
    private UserService userService;

    @GetMapping("/getOne")
    public ResponseEntity<?> getOne(@RequestParam Long idUser, @RequestParam Long idEvent) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        Optional<LifeEvents> lifeEvents = lifeEventsService.findById(idEvent);
        if (!lifeEvents.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_EVENT, idEvent),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lifeEvents.get(), HttpStatus.OK);
    }

    // Tạo sự kiện
    @PostMapping("/createLifeEvents")
    public ResponseEntity<?> createLifeEvents(@RequestParam Long idUser, @RequestBody LifeEvents lifeEvents) {
        if (lifeEvents.getTimeline() == null) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField("Timeline"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(lifeEvents.getWork())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField("work"), HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> responseEntity = Common.handlerWordsLanguage(lifeEvents);
        if (null != responseEntity) return responseEntity;
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        lifeEvents.setCreateAt(new Date());
        lifeEvents.setUser(userOptional.get());
        lifeEvents.setStatus(Constants.STATUS_PUBLIC);
        if (StringUtils.isEmpty(lifeEvents.getWork())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.WORK),
                    HttpStatus.BAD_REQUEST);
        }
        lifeEventsService.save(lifeEvents);
        return new ResponseEntity<>(lifeEvents, HttpStatus.OK);
    }

    // Sửa sự kiện
    @PutMapping("/update")
    public ResponseEntity<?> updateLifeEvents(@RequestParam Long idUser,
                                              @RequestParam Long idEvent,
                                              @RequestBody LifeEvents lifeEvents) {
        try {
            Optional<LifeEvents> lifeEventsOptional = lifeEventsService.findById(idEvent);
            if (!lifeEventsOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_EVENT, idEvent),
                        HttpStatus.NOT_FOUND);
            }
            Optional<User> userOptional = userService.findById(idUser);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                        HttpStatus.NOT_FOUND);
            }
            ResponseEntity<?> responseEntity = Common.handlerWordsLanguage(lifeEvents);
            if (null != responseEntity) return responseEntity;
            if (lifeEvents.getWork() != null) {
                lifeEventsOptional.get().setWork(lifeEvents.getWork());
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (lifeEvents.getTimeline() != null) {
                lifeEventsOptional.get().setTimeline(lifeEvents.getTimeline());
            }
            lifeEventsOptional.get().setEditAt(new Date());
            lifeEventsService.save(lifeEventsOptional.get());
            return new ResponseEntity<>(lifeEventsOptional.get(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Xóa sự kiện
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLifeEvents(@RequestParam Long idUser, @RequestParam Long idEvent) {
        try {
            Optional<LifeEvents> lifeEventsOptional = lifeEventsService.findById(idEvent);
            if (!lifeEventsOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_EVENT, idEvent),
                        HttpStatus.NOT_FOUND);
            }
            Optional<User> userOptional = userService.findById(idUser);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                        HttpStatus.NOT_FOUND);
            }
            lifeEventsService.delete(lifeEventsOptional.get());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findListByIdUser")
    public ResponseEntity<?> findListByIdUser(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<LifeEvents> list = lifeEventsService.findLifeEventsByUserId(idUser);
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
