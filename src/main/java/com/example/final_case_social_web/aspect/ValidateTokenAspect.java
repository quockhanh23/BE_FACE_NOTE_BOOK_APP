package com.example.final_case_social_web.aspect;


import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.exeption.TokenInvalidException;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class ValidateTokenAspect {

    @Autowired
    private UserService userService;

    private Map<String, Object> getMethodSignature(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> parameterMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            parameterMap.put(parameterNames[i], args[i]);
        }
        String methodName = methodSignature.getMethod().getName();
        log.info("Calling method: " + methodName + " with parameters: " + parameterMap);
        return parameterMap;
    }

    @Before("execution(* com.example.final_case_social_web.controller.AdminRestController.*(..))")
    public void beforeAdminRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token AdminRestController Methods");
        Map<String, Object> parameterMap = getMethodSignature(joinPoint);
        if (parameterMap.containsKey("authorization") && parameterMap.containsKey("idAdmin")) {
            Object authorizationValue = parameterMap.get("authorization");
            Object idAdmin = parameterMap.get("idAdmin");
            log.info("Authorization value: " + authorizationValue);

            if (!userService.errorToken(authorizationValue.toString(), Long.valueOf(idAdmin.toString()))) {
                throw new TokenInvalidException(Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase());
            }
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token AdminRestController Methods");
    }

    @Before("execution(* com.example.final_case_social_web.controller.UserController.*(..))")
    public void beforeUserControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token UserController Methods");
        Map<String, Object> parameterMap = getMethodSignature(joinPoint);
        if (parameterMap.containsKey("authorization") && parameterMap.containsKey("idUser")) {
            Object authorizationValue = parameterMap.get("authorization");
            Object idUser = parameterMap.get("idUser");
            if (!userService.errorToken(authorizationValue.toString(), Long.valueOf(idUser.toString()))) {
                throw new TokenInvalidException(Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase());
            }
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token UserController Methods");
    }
}
