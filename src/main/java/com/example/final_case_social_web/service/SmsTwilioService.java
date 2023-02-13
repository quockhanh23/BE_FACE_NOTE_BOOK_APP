package com.example.final_case_social_web.service;

import java.util.List;

public interface SmsTwilioService {

    void sendSMS();

    void createNewPhoneNumberInTwilio(String phoneNumber);

    void deletePhoneNumberInTwilio(String phoneNumber);

    List<String> listNumberPhoneTwilio();
}
