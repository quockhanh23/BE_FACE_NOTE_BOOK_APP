package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortNewsListDTO {
    private Long id;
    private String content;
    private Date createAt;
    private int expired;
    private int remaining;
    private String image;
    private String status;
    private UserNotificationDTO userDTO;
}
