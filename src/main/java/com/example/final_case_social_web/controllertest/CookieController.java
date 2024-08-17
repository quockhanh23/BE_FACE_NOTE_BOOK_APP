package com.example.final_case_social_web.controllertest;


import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/cookies")
public class CookieController {

    @GetMapping("/hello")
    public String hello(@CookieValue(value = "hitCounter", defaultValue = "0") Long hitCounter,
                        HttpServletResponse response, HttpServletRequest request) {
        hitCounter++;
        Cookie cookie = new Cookie("hitCounter", hitCounter.toString());
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        //get all cookies
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("hitCounter")) {
                System.out.println("cookieValue:" + cookies[i].getValue());
                break;
            }
        }
        return "hello";
    }
}
