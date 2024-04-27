package com.example.final_case_social_web.exeption;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException(String msg) {
        super(msg);
    }
}
