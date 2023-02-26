package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String status;
    private UserNotificationDTO idSendTo;
    private UserNotificationDTO idAction;
    private Long typeId;
    private String type;
    private Date createAt;
}
