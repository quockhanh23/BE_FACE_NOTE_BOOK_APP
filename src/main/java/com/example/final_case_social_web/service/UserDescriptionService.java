package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.UserDescription;

import java.util.List;
import java.util.Optional;

public interface UserDescriptionService {

    List<UserDescription> findAllByUserId(Long user_id);

    void save(UserDescription userDescription);

    void delete(UserDescription userDescription);

    Optional<UserDescription> findById(Long id);
}
