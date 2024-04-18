package com.example.final_case_social_web.component;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.example.final_case_social_web.controller.*.*(..))")
    public void beforeServiceMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @Before");
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("A method " + className + "." + methodName + " is about to be called");
    }

    @Before("execution(* com.example.final_case_social_web.service.*.*(..))")
    public void beforeServiceMethods() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @Before");
        log.info("A service method is about to be called");
    }

    @AfterReturning("execution(* com.example.final_case_social_web.service.*.*(..))")
    public void afterReturningServiceMethods() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @AfterReturning");
        log.info("A service method has returned successfully");
    }

    @AfterThrowing(pointcut = "execution(* com.example.final_case_social_web.controller.*.*(..))", throwing = "ex")
    public void afterThrowingServiceMethods(Exception ex) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @AfterThrowing");
        log.info("A service method has thrown an exception: " + ex.getMessage());
    }

    @After("execution(* com.example.final_case_social_web.service.*.*(..))")
    public void afterServiceMethods() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @After");
        log.info("A service method has finished (with or without exception)");
    }

    @Around("execution(* com.example.final_case_social_web.controller.*.*(..))")
    public Object aroundServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> @Around");
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("A method " + className + "." + methodName + " is about to be called");
        final double startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        final double elapsedTimeMillis = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillis);
        log.info("After invoking the service method " + className + "." + methodName);
        return result;
    }
}
