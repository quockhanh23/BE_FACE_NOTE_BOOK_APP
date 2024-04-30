package com.example.final_case_social_web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/caches")
@Slf4j
public class CachingController {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/getAllCaches")
    public ResponseEntity<?> getAllCaches() {
        return new ResponseEntity<>(cacheManager.getCacheNames(), HttpStatus.OK);
    }

    @GetMapping("/getCache")
    public ResponseEntity<?> getCache(@RequestParam String cacheName) {
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if (Objects.isNull(cache)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cache, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("clearAllCaches")
    public ResponseEntity<?> clearAllCaches() {
        Iterable<String> iterable = cacheManager.getCacheNames();
        iterable.forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
