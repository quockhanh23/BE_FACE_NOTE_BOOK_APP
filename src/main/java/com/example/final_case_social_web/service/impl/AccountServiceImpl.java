package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.UserRepository;
import com.example.final_case_social_web.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@Service(value = "accountServiceImpl")
//@Scope(value = "singleton")
public class AccountServiceImpl implements GeneralService<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public User save(User user) {
        return userRepository.save(user);
    }

}
