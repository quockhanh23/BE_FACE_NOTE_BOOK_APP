package com.example.final_case_social_web.service;


import com.example.final_case_social_web.model.VerificationToken;

import java.util.List;

public interface VerificationTokenService {

    void save(VerificationToken token);

    List<VerificationToken> findAll();
}
