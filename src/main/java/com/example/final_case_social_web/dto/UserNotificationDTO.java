package com.example.final_case_social_web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationDTO {
    private Long id;
    private String fullName;
    private String avatar;
    private String status;
}
