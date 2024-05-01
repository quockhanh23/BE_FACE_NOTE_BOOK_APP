package com.example.final_case_social_web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private void getInformation(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("A method " + className + "." + methodName + " is about to be called");
    }

    @Before("execution(* com.example.final_case_social_web.controller.*.*(..))")
    public void beforeControllerMethods(JoinPoint joinPoint) {
        getInformation(joinPoint);
    }

    @Before("execution(* com.example.final_case_social_web.service.*.*(..))")
    public void beforeServiceMethods(JoinPoint joinPoint) {
        getInformation(joinPoint);
    }

    @AfterReturning("execution(* com.example.final_case_social_web.service.*.*(..))")
    public void afterReturningServiceMethods() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @AfterReturning");
        log.info("A service method has returned successfully");
    }

    @AfterThrowing(pointcut = "execution(* com.example.final_case_social_web.controller.*.*(..))", throwing = "ex")
    public void afterThrowingControllerMethods(Exception ex) {
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @afterThrowingControllerMethods");
        log.error("A method has thrown an exception: " + ex.getMessage());
        ex.printStackTrace();
    }

    @AfterThrowing(pointcut = "execution(* com.example.final_case_social_web.service.*.*(..))", throwing = "ex")
    public void afterThrowingServiceMethods(Exception ex) {
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @afterThrowingServiceMethods");
        log.error("A method has thrown an exception: " + ex.getMessage());
        ex.printStackTrace();
    }

    @After("execution(* com.example.final_case_social_web.service.*.*(..))")
    public void afterServiceMethods() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @After");
        log.info("A service method has finished (with or without exception)");
    }
}
