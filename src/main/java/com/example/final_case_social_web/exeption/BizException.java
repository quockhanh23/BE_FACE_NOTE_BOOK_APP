package com.example.final_case_social_web.exeption;

import com.example.final_case_social_web.notification.ResponseNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizException extends RuntimeException {
    private String code;
    private ResponseNotification responseNotification;
}
