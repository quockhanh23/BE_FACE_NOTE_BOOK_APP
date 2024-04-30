package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.UserDescription;
import com.example.final_case_social_web.repository.UserDescriptionRepository;
import com.example.final_case_social_web.service.UserDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDescriptionServiceImpl implements UserDescriptionService {

    @Autowired
    private UserDescriptionRepository userDescriptionRepository;

    @Override
    @Cacheable(cacheNames = "findAllByUserId", key = "#idUser")
    public List<UserDescription> findAllByUserId(Long user_id) {
        return userDescriptionRepository.findAllByUserIdOrderByCreateAtDesc(user_id);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllByUserId"}, allEntries = true)
    public void save(UserDescription userDescription) {
        userDescriptionRepository.save(userDescription);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllByUserId"}, allEntries = true)
    public void delete(UserDescription userDescription) {
        userDescriptionRepository.delete(userDescription);
    }

    @Override
    public Optional<UserDescription> findById(Long id) {
        return userDescriptionRepository.findById(id);
    }
}
