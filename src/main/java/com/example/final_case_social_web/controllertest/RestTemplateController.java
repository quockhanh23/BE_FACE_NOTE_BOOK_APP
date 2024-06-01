package com.example.final_case_social_web.controllertest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/rest-template")
public class RestTemplateController {

    // Mặc định response sẽ được trả về ở định dạng XML
    @Autowired
    private RestTemplate restTemplate;

    // Lấy ra các biến từ biến môi trường
    @Autowired
    private Environment env;

    @GetMapping("/call")
    public ResponseEntity<String> callOtherApi() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String test = env.getProperty("spring.redis.host");
        String url = "http://localhost:8080/api/listImageDefault";
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @GetMapping("/callOtherApp")
    public ResponseEntity<String> callOtherApp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = "https://api.restful-api.dev/objects";
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @PostMapping
    public String callExternalAPI() {
        String body = "{\"name\": \"Apple OD\", \"data\": { \"Generation\": \"9th\", \"Price\": \"519.99\", \"Capacity\": \"256 GB\" }}";
        String url = "https://api.restful-api.dev/objects";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }
}
