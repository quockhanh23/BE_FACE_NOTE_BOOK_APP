package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.NotificationDTO;
import com.example.final_case_social_web.dto.UserNotificationDTO;
import com.example.final_case_social_web.model.Notification;
import com.example.final_case_social_web.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/getAllNotificationByIdSenTo")
    public ResponseEntity<?> getAllNotificationByIdSenTo(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendTo(idSenTo);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(notificationList)) {
            for (Notification notification : notificationList) {
                NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
                UserNotificationDTO userAction = modelMapper.map(notification.getIdAction(), UserNotificationDTO.class);
                UserNotificationDTO userSendTo = modelMapper.map(notification.getIdSendTo(), UserNotificationDTO.class);
                notificationDTO.setIdAction(userAction);
                notificationDTO.setIdSendTo(userSendTo);
                notificationDTOS.add(notificationDTO);
            }
        }
        return new ResponseEntity<>(notificationDTOS, HttpStatus.OK);
    }

    @GetMapping("/findAllByIdSendToNotSeen")
    public ResponseEntity<?> findAllByIdSendToNotSeen(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendToNotSeen(idSenTo);
        return new ResponseEntity<>(notificationList.size(), HttpStatus.OK);
    }

    // Đã xem tất cả thông báo
    @GetMapping("/seenAll")
    public ResponseEntity<?> seenAll(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendTo(idSenTo);
        if (!CollectionUtils.isEmpty(notificationList)) {
            notificationList.forEach(notification -> notification.setStatus(Constants.Notification.STATUS_SEEN));
            notificationService.saveAll(notificationList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Xóa tất cả thông báo
    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAll(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendTo(idSenTo);
        if (!CollectionUtils.isEmpty(notificationList)) {
            notificationService.deleteAll(notificationList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
