package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationDTO {
    private Long id;
    private String fullName;
    private String avatar;
    private String status;
}
