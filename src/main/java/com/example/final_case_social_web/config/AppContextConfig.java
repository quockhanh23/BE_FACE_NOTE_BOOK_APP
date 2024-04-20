package com.example.final_case_social_web.config;

import com.example.final_case_social_web.component.RequestLimitingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class AppContextConfig {
    @Bean
    public MappedInterceptor myInterceptor() {
        return new MappedInterceptor(null, new RequestLimitingInterceptor());
    }
}
