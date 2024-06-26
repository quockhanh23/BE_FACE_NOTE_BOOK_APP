package com.example.final_case_social_web.controllertest;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.config.RabbitMQConfig;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/rabbits")
@Slf4j
public class RabbitMQController { // Producers

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/push-message")
    ResponseEntity<?> sendMessage(@RequestParam(value = "message", defaultValue = "Test push message") String message)
            throws JsonProcessingException {
        List<User> userList = userService.findAllRoleUser();
        List<UserDTO> userDTOList = userService.copyListDTO(userList);
        ObjectMapper objectMapper = Common.intObjectMapper();
        String json = objectMapper.writeValueAsString(userDTOList);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, json);
        System.out.println("Sent message: " + message);
        return new ResponseEntity<>("push-message: " + message, HttpStatus.OK);
    }
}
