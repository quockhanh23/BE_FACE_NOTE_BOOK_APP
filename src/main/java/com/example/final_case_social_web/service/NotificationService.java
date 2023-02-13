package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.Notification;
import com.example.final_case_social_web.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationService {

    void delete(Notification notification);

    void deleteAll(List<Notification> notifications);

    void save(Notification notification);

    void saveAll(List<Notification> notifications);

    Notification createDefault(User idSendTo, User idAction, String title, Long typeId, String type);

    List<Notification> findAllByIdSendTo(@Param("idSendTo") Long idSendTo);

    List<Notification> findAllByIdSendToNotSeen(@Param("idSendTo") Long idSendTo);
}
