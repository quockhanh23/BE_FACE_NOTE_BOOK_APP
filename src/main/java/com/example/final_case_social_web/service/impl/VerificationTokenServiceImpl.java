package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.VerificationToken;
import com.example.final_case_social_web.repository.VerificationTokenRepository;
import com.example.final_case_social_web.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void save(VerificationToken token) {
        verificationTokenRepository.save(token);
    }


    @Override
    public List<VerificationToken> findAll() {
        return verificationTokenRepository.findAll();
    }
}
