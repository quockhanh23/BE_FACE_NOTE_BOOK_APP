package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.Notification;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.NotificationRepository;
import com.example.final_case_social_web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @CacheEvict(cacheNames = {"findAllByIdSendTo", "findAllByIdSendToNotSeen"}, allEntries = true)
    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllByIdSendTo", "findAllByIdSendToNotSeen"}, allEntries = true)
    public void deleteAll(List<Notification> notifications) {
        notificationRepository.deleteAll(notifications);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllByIdSendTo", "findAllByIdSendToNotSeen"}, allEntries = true)
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllByIdSendTo", "findAllByIdSendToNotSeen"}, allEntries = true)
    public void saveAll(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Override
    public Notification createDefault(User idSendTo, User idAction, String title, Long typeId, String type) {
        Notification notification = new Notification();
        notification.setStatus(Constants.Notification.STATUS_NOT_SEEN);
        notification.setIdAction(idAction);
        notification.setIdSendTo(idSendTo);
        notification.setTypeId(typeId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setCreateAt(new Date());
        return notification;
    }

    @Override
    @Cacheable(cacheNames = "findAllByIdSendTo", key = "#idSendTo")
    public List<Notification> findAllByIdSendTo(Long idSendTo) {
        return notificationRepository.findAllByIdSendTo(idSendTo);
    }

    @Override
    @Cacheable(cacheNames = "findAllByIdSendToNotSeen", key = "#idSendTo")
    public List<Notification> findAllByIdSendToNotSeen(Long idSendTo) {
        return notificationRepository.findAllByIdSendToNotSeen(idSendTo);
    }
}
