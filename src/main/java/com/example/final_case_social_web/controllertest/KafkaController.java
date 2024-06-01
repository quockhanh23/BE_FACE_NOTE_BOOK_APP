package com.example.final_case_social_web.controllertest;

import com.example.final_case_social_web.component.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "api/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @GetMapping(value = "/push-message")
    public ResponseEntity<?> testSendMessage(@RequestParam String message) {
        kafkaProducerService.sendMessage("my-topic", message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
