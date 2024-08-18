package com.example.final_case_social_web.controllertest;

import com.example.final_case_social_web.dto.EmailContent;
import com.example.final_case_social_web.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestParam String email) {
        EmailContent emailContent = new EmailContent();
        emailContent.setEmail(email);
        emailContent.setTitle("Chào mừng");
        emailService.sendEmailWelCome(emailContent);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
