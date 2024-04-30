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
import java.util.Objects;

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

    private void parameterMapWithIdAndAuthorization(JoinPoint joinPoint, String idType) {
        Map<String, Object> parameterMap = getMethodSignature(joinPoint);
        if (parameterMap.containsKey("authorization") && parameterMap.containsKey(idType)) {
            Object authorizationValue = parameterMap.get("authorization");
            Object id = parameterMap.get(idType);
            if (Objects.isNull(authorizationValue) || Objects.isNull(id)) return;
            if (!userService.errorToken(authorizationValue.toString(), Long.valueOf(id.toString()))) {
                throw new TokenInvalidException(Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase());
            }
        }
    }

    @Before("execution(* com.example.final_case_social_web.controller.AdminRestController.*(..))")
    public void beforeAdminRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token AdminRestController Methods");
        parameterMapWithIdAndAuthorization(joinPoint, "idAdmin");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token AdminRestController Methods");
    }

    @Before("execution(* com.example.final_case_social_web.controller.UserController.*(..)) && !execution(* com.example.final_case_social_web.controller.UserController.passwordRetrieval(..))")
    public void beforeUserControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token UserController Methods");
        parameterMapWithIdAndAuthorization(joinPoint, "idUser");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token UserController Methods");
    }

    @Before("execution(* com.example.final_case_social_web.controller.ImageRestController.*(..)) && !execution(* com.example.final_case_social_web.controller.ImageRestController.findAllImages(..))")
    public void beforeImageRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token ImageRestController Methods");
        parameterMapWithIdAndAuthorization(joinPoint, "idUser");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token ImageRestController Methods");
    }

    @Before("execution(* com.example.final_case_social_web.controller.PostRestController.*(..)) " +
            "&& !execution(* com.example.final_case_social_web.controller.PostRestController.allPostPublic(..)) " +
            "&& !execution(* com.example.final_case_social_web.controller.PostRestController.findOnePostById(..))")
    public void beforePostRestControllerMethods(JoinPoint joinPoint) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start Checking Token PostRestController Methods");
        parameterMapWithIdAndAuthorization(joinPoint, "idUser");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Checking Token PostRestController Methods");
    }
}
