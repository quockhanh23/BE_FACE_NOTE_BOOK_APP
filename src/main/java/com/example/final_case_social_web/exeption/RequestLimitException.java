package com.example.final_case_social_web.exeption;

public class RequestLimitException extends RuntimeException {
    public RequestLimitException(String msg) {
        super(msg);
    }
}
