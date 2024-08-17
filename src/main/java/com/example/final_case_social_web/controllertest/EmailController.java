package com.example.final_case_social_web.controllertest;

import com.example.final_case_social_web.common.MessageResponse;
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
        emailService.sendMail(email, "Đã gửi Email", MessageResponse.Email.THANK);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
