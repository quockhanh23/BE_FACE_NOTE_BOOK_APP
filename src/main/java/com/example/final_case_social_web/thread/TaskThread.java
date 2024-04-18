package com.example.final_case_social_web.thread;


import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.UserRepository;

import java.util.List;

public class TaskThread implements Runnable {
    private final UserRepository userRepository;
    public List<User> users;

    public TaskThread(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        users = userRepository.findAll();
    }
}
