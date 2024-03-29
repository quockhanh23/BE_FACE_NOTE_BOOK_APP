package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.UserComplaints;
import com.example.final_case_social_web.repository.UserComplaintsRepository;
import com.example.final_case_social_web.service.UserComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserComplaintsServiceImpl implements UserComplaintsService {

    @Autowired
    private UserComplaintsRepository userComplaintsRepository;

    @Override
    public Optional<UserComplaints> findById(Long id) {
        return userComplaintsRepository.findById(id);
    }

    @Override
    public UserComplaints save(UserComplaints userComplaints) {
        return userComplaintsRepository.save(userComplaints);
    }
}
