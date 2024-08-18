package com.example.final_case_social_web.service;

import com.example.final_case_social_web.dto.EmailContent;

public interface EmailService {
    void sendMail(String email, String title, String content);

    void sendEmailWelCome(EmailContent emailContent);
}
