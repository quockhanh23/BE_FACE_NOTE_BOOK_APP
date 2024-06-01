package com.example.final_case_social_web.controllertest;

import com.example.final_case_social_web.service.SmsTwilioService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/sms")
@Slf4j
@RequiredArgsConstructor
public class SmsController {

//    @Value("${USER_ID}")
//    private String user_id;
//
//    @Value("${TWILIO_AUTH_TOKEN}")
//    private String token;
    @Autowired
    private SmsTwilioService smsTwilioService;
    public static final String ACCOUNT_SID = "AC77cc1dc2b383bac927ad7bf4cf7d1dc5";
    public static final String AUTH_TOKEN = "30e13bd3a7085eb89ff49d7ee64f99be";

    @GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(new PhoneNumber("+84353413219"), "MG42bec33a1f4c63d9356718ba222e3eb9"
                , "Alo alo 📞").create();
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/listPhone")
    public ResponseEntity<?> listPhone() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        List<String> list = smsTwilioService.listNumberPhoneTwilio();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(value = "/addNewPhoneToTwilio")
    public ResponseEntity<?> addNewPhoneToTwilio(String numberPhone) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        smsTwilioService.createNewPhoneNumberInTwilio(numberPhone);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
