package com.example.final_case_social_web.service;

public interface RedisService {

    void setData(String key, String value);

    void setTimeToLive(String key, long timeOutInDays);
}
