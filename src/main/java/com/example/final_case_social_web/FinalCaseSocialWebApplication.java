package com.example.final_case_social_web;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.Role;
import com.example.final_case_social_web.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinalCaseSocialWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalCaseSocialWebApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository) {
        return args ->
        {
            if (roleRepository.findByName(Constants.Roles.ROLE_ADMIN) == null) {
                roleRepository.save(new Role(Constants.Roles.ROLE_ADMIN));
            }
            if (roleRepository.findByName(Constants.Roles.ROLE_USER) == null) {
                roleRepository.save(new Role(Constants.Roles.ROLE_USER));
            }
        };
    }
}
