package com.example.final_case_social_web.component;

import com.example.final_case_social_web.config.RabbitMQConfig;
import com.example.final_case_social_web.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectMapper intObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processMessage(String message) {
        ObjectMapper objectMapper = intObjectMapper();
        try {
            UserDTO[] myObjects = objectMapper.readValue(message, UserDTO[].class);
            for (UserDTO obj : myObjects) {
                System.out.println(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
