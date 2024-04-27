package com.example.final_case_social_web.aspect;

import com.example.final_case_social_web.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceLogAspect {

    @Around("execution(* com.example.final_case_social_web.controller.*.*(..))")
    public Object aroundServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("A method " + className + "." + methodName + " is about to be called");
        final double startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        final double elapsedTimeMillis = System.currentTimeMillis();
        log.info("Execution time method: " + className + "." + methodName + "()");
        Common.executionTime(startTime, elapsedTimeMillis);
        log.info("After invoking the service method " + className + "." + methodName);
        return result;
    }
}
