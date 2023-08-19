package com.example.final_case_social_web;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
    @Test
    public void testSomething() {
        int result = 1 + 1;
        assertEquals(2, result);
    }
}
