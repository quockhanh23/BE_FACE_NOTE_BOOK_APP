package com.example.final_case_social_web.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class AppContextConfiguration {
    @Bean
    public MappedInterceptor myInterceptor() {
        return new MappedInterceptor(null, new RequestLimitingInterceptor());
    }
}
